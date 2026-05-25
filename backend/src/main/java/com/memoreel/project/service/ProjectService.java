package com.memoreel.project.service;

import com.memoreel.project.dto.CreateProjectRequest;
import com.memoreel.project.dto.ProjectDetailDto;
import com.memoreel.project.dto.ProjectListResponse;
import com.memoreel.project.dto.ProjectSummaryDto;
import com.memoreel.project.model.ProjectRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;
    private static final long MAX_VIDEO_SIZE = 50L * 1024 * 1024;
    private static final List<String> IMAGE_MIME_TYPES = List.of("image/jpeg", "image/png");
    private static final List<String> VIDEO_MIME_TYPES = List.of("video/mp4", "video/quicktime");
    private static final List<String> SUPPORTED_OCCASIONS = List.of(
            "birthday", "anniversary", "wedding", "graduation", "baby_shower",
            "housewarming", "retirement", "farewell", "trip_memory", "friendship",
            "reunion", "milestone", "achievement", "memorial", "condolence",
            "remembrance_day", "custom"
    );

    private final Map<Long, ProjectRecord> projects = new LinkedHashMap<>();
    private final AtomicLong projectSequence = new AtomicLong(1);
    private final AtomicLong mediaSequence = new AtomicLong(1);
    private final AtomicLong jobSequence = new AtomicLong(1);
    private static final String FFMPEG_EXECUTABLE = "C:\\Users\\ReethuJoseph\\Downloads\\ffmpeg-8.1.1-essentials_build\\ffmpeg-8.1.1-essentials_build\\bin\\ffmpeg.exe";
    private final Path storageRoot = Path.of("storage");

    public ProjectSummaryDto createProject(Long userId, CreateProjectRequest request) {
        validateOccasionType(request.getType());

        LocalDateTime now = LocalDateTime.now();
        long id = projectSequence.getAndIncrement();

        ProjectRecord record = ProjectRecord.builder()
                .id(id)
                .userId(userId)
                .title(request.getTitle())
                .type(normalizeType(request.getType()))
                .status("draft")
                .privacy("public")
                .metadata(request.getMetadata() == null ? new LinkedHashMap<>() : new LinkedHashMap<>(request.getMetadata()))
                .media(new ArrayList<>())
                .video(null)
                .videoJob(null)
                .publicLink(null)
                .qrCode(null)
                .createdAt(now)
                .updatedAt(now)
                .publishedAt(null)
                .build();

        projects.put(id, record);
        return toSummary(record);
    }

    public ProjectListResponse listProjects(Long userId, String status, String type, int page, int limit) {
        List<ProjectSummaryDto> filtered = projects.values().stream()
                .filter(project -> project.getUserId().equals(userId))
                .filter(project -> status == null || status.isBlank() || project.getStatus().equalsIgnoreCase(status))
                .filter(project -> type == null || type.isBlank() || project.getType().equalsIgnoreCase(type))
                .sorted(Comparator.comparing(ProjectRecord::getUpdatedAt).reversed())
                .map(this::toSummary)
                .collect(Collectors.toList());

        int safeLimit = Math.max(1, Math.min(limit, 50));
        int safePage = Math.max(1, page);
        int total = filtered.size();
        int fromIndex = Math.min((safePage - 1) * safeLimit, total);
        int toIndex = Math.min(fromIndex + safeLimit, total);

        Map<String, Object> pagination = new LinkedHashMap<>();
        pagination.put("page", safePage);
        pagination.put("limit", safeLimit);
        pagination.put("total", total);
        pagination.put("totalPages", total == 0 ? 0 : (int) Math.ceil((double) total / safeLimit));

        return ProjectListResponse.builder()
                .projects(filtered.subList(fromIndex, toIndex))
                .pagination(pagination)
                .build();
    }

    public ProjectDetailDto getProject(Long userId, Long projectId) {
        ProjectRecord record = requireOwnedProject(userId, projectId);
        return toDetail(record);
    }

    public ProjectDetailDto uploadMedia(Long userId, Long projectId, List<MultipartFile> files) {
        ProjectRecord project = requireOwnedProject(userId, projectId);
        if (!"draft".equalsIgnoreCase(project.getStatus()) && !"failed".equalsIgnoreCase(project.getStatus())) {
            throw new IllegalStateException("Media can only be uploaded while project is draft or failed");
        }
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("At least one file is required");
        }

        List<Map<String, Object>> media = project.getMedia() == null ? new ArrayList<>() : new ArrayList<>(project.getMedia());
        int nextOrderIndex = media.stream()
                .map(item -> ((Number) item.get("orderIndex")).intValue())
                .max(Integer::compareTo)
                .orElse(0) + 1;

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            String mimeType = normalizeMimeType(file.getContentType());
            String mediaType = determineMediaType(mimeType);
            validateFile(file, mediaType, mimeType);

            long mediaId = mediaSequence.getAndIncrement();
            String storedFileName = mediaId + "-" + sanitizeFilename(file.getOriginalFilename());
            Path mediaDirectory = storageRoot.resolve("users")
                    .resolve(String.valueOf(userId))
                    .resolve("projects")
                    .resolve(String.valueOf(projectId))
                    .resolve("media");
            createDirectories(mediaDirectory);
            Path targetFile = mediaDirectory.resolve(storedFileName);
            transferFile(file, targetFile);

            Map<String, Object> mediaItem = new LinkedHashMap<>();
            mediaItem.put("id", mediaId);
            mediaItem.put("type", mediaType);
            mediaItem.put("url", "/projects/" + projectId + "/media/" + storedFileName);
            mediaItem.put("thumbnailUrl", mediaType.equals("video") ? null : "/projects/" + projectId + "/media/" + storedFileName);
            mediaItem.put("orderIndex", nextOrderIndex++);
            mediaItem.put("size", file.getSize());
            mediaItem.put("mimeType", mimeType);
            mediaItem.put("originalFilename", file.getOriginalFilename());
            mediaItem.put("uploadedAt", LocalDateTime.now());
            media.add(mediaItem);
        }

        if (media.isEmpty()) {
            throw new IllegalArgumentException("No valid files were uploaded");
        }

        project.setMedia(media.stream()
                .sorted(Comparator.comparingInt(item -> ((Number) item.get("orderIndex")).intValue()))
                .collect(Collectors.toList()));
        project.setStatus("draft");
        project.setUpdatedAt(LocalDateTime.now());
        return toDetail(project);
    }

    public ProjectDetailDto generateVideo(Long userId, Long projectId) {
        ProjectRecord project = requireOwnedProject(userId, projectId);
        if (!"draft".equalsIgnoreCase(project.getStatus()) && !"failed".equalsIgnoreCase(project.getStatus())) {
            throw new IllegalStateException("Can only generate video from draft or failed state");
        }
        if (project.getMedia() == null || project.getMedia().isEmpty()) {
            throw new IllegalStateException("Upload media before generating a video");
        }

        LocalDateTime now = LocalDateTime.now();
        long jobId = jobSequence.getAndIncrement();
        project.setStatus("processing");
        project.setVideoJob(createVideoJob(jobId, "processing", now, null, null));
        project.setUpdatedAt(now);

        try {
            Path videoDirectory = storageRoot.resolve("videos");
            createDirectories(videoDirectory);
            Path outputFile = videoDirectory.resolve("project-" + projectId + ".mp4");

            List<Map<String, Object>> orderedMedia = new ArrayList<>(project.getMedia());
            orderedMedia.sort(Comparator.comparingInt(item -> ((Number) item.get("orderIndex")).intValue()));

            int duration = renderVideoWithFfmpeg(userId, projectId, orderedMedia, outputFile);

            project.setVideo(createVideoMap(projectId, duration, now));
            project.setVideoJob(createVideoJob(jobId, "completed", now, LocalDateTime.now(), null));
            project.setStatus("completed");
            project.setUpdatedAt(LocalDateTime.now());
        } catch (Exception exception) {
            project.setStatus("failed");
            project.setVideoJob(createVideoJob(jobId, "failed", now, LocalDateTime.now(), exception.getMessage()));
            project.setUpdatedAt(LocalDateTime.now());
            throw new IllegalStateException("Video generation failed: " + exception.getMessage());
        }

        return toDetail(project);
    }

    public ProjectDetailDto publishProject(Long userId, Long projectId) {
        ProjectRecord project = requireOwnedProject(userId, projectId);
        if (!"completed".equalsIgnoreCase(project.getStatus())) {
            throw new IllegalStateException("Only completed projects can be published");
        }
        if (project.getVideo() == null) {
            throw new IllegalStateException("Generated video is required before publish");
        }

        LocalDateTime now = LocalDateTime.now();
        String slug = createPermanentSlug(projectId);
        String publicUrl = "http://localhost:8080/public/" + slug;
        project.setPublicLink(createPublicLink(slug, publicUrl));
        project.setQrCode(createQrCode(publicUrl));
        project.setStatus("published");
        project.setPublishedAt(now);
        project.setUpdatedAt(now);

        return toDetail(project);
    }

    public ProjectDetailDto getPublicProject(String slug) {
        ProjectRecord record = projects.values().stream()
                .filter(project -> "published".equalsIgnoreCase(project.getStatus()))
                .filter(project -> project.getPublicLink() != null)
                .filter(project -> slug.equals(project.getPublicLink().get("slug")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Public project not found"));
        return toDetail(record);
    }

    public Path resolveProjectMediaPath(Long userId, Long projectId, String fileName) {
        requireOwnedProject(userId, projectId);
        Path filePath = storageRoot.resolve("users")
                .resolve(String.valueOf(userId))
                .resolve("projects")
                .resolve(String.valueOf(projectId))
                .resolve("media")
                .resolve(fileName)
                .normalize();

        if (!filePath.startsWith(storageRoot.resolve("users").resolve(String.valueOf(userId)).normalize())) {
            throw new SecurityException("Invalid media path");
        }
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("Media file not found");
        }
        return filePath;
    }

    public Path resolveGeneratedVideoPath(Long projectId) {
        ProjectRecord record = projects.get(projectId);
        if (record == null || record.getVideo() == null) {
            throw new IllegalArgumentException("Generated video not found");
        }

        Path filePath = storageRoot.resolve("videos")
                .resolve("project-" + projectId + ".mp4")
                .normalize();

        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("Generated video file not found");
        }
        return filePath;
    }

    private ProjectRecord requireOwnedProject(Long userId, Long projectId) {
        ProjectRecord record = projects.get(projectId);
        if (record == null) {
            throw new IllegalArgumentException("Project not found");
        }
        if (!record.getUserId().equals(userId)) {
            throw new SecurityException("You are not allowed to access this project");
        }
        return record;
    }

    private ProjectSummaryDto toSummary(ProjectRecord record) {
        String thumbnail = record.getMedia() != null && !record.getMedia().isEmpty()
                ? String.valueOf(record.getMedia().get(0).get("url"))
                : "https://placehold.co/600x400?text=" + record.getType();

        return ProjectSummaryDto.builder()
                .id(record.getId())
                .title(record.getTitle())
                .type(record.getType())
                .status(record.getStatus())
                .thumbnail(thumbnail)
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }

    private ProjectDetailDto toDetail(ProjectRecord record) {
        return ProjectDetailDto.builder()
                .id(record.getId())
                .title(record.getTitle())
                .type(record.getType())
                .status(record.getStatus())
                .privacy(record.getPrivacy())
                .metadata(record.getMetadata())
                .media(record.getMedia())
                .video(record.getVideo())
                .videoJob(record.getVideoJob())
                .publicLink(record.getPublicLink())
                .qrCode(record.getQrCode())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .publishedAt(record.getPublishedAt())
                .build();
    }

    private void validateOccasionType(String type) {
        if (type == null || type.isBlank() || !SUPPORTED_OCCASIONS.contains(normalizeType(type))) {
            throw new IllegalArgumentException("Invalid occasion type");
        }
    }

    private String normalizeType(String type) {
        return type.trim().toLowerCase();
    }

    private String normalizeMimeType(String mimeType) {
        return mimeType == null ? "" : mimeType.trim().toLowerCase();
    }

    private String determineMediaType(String mimeType) {
        if (IMAGE_MIME_TYPES.contains(mimeType)) {
            return "image";
        }
        if (VIDEO_MIME_TYPES.contains(mimeType)) {
            return "video";
        }
        throw new IllegalArgumentException("Unsupported media type: " + mimeType);
    }

    private void validateFile(MultipartFile file, String mediaType, String mimeType) {
        if ("image".equals(mediaType) && file.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Image exceeds 5MB limit: " + file.getOriginalFilename());
        }
        if ("video".equals(mediaType) && file.getSize() > MAX_VIDEO_SIZE) {
            throw new IllegalArgumentException("Video exceeds 50MB limit: " + file.getOriginalFilename());
        }
        if ("image".equals(mediaType) && !IMAGE_MIME_TYPES.contains(mimeType)) {
            throw new IllegalArgumentException("Unsupported image MIME type");
        }
        if ("video".equals(mediaType) && !VIDEO_MIME_TYPES.contains(mimeType)) {
            throw new IllegalArgumentException("Unsupported video MIME type");
        }
    }

    private String sanitizeFilename(String filename) {
        String safe = filename == null || filename.isBlank() ? "file" : filename;
        return safe.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private void createDirectories(Path directory) {
        try {
            Files.createDirectories(directory);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to initialize storage directory", exception);
        }
    }

    private void transferFile(MultipartFile file, Path targetFile) {
        try {
            file.transferTo(targetFile);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to store uploaded file: " + file.getOriginalFilename(), exception);
        }
    }

    private Map<String, Object> createVideoJob(long jobId, String status, LocalDateTime startedAt, LocalDateTime completedAt, String errorMessage) {
        Map<String, Object> job = new LinkedHashMap<>();
        job.put("id", jobId);
        job.put("status", status);
        job.put("startedAt", startedAt);
        job.put("completedAt", completedAt);
        if (errorMessage != null) {
            job.put("errorMessage", errorMessage);
        }
        return job;
    }

    private Map<String, Object> createVideoMap(Long projectId, int duration, LocalDateTime createdAt) {
        Map<String, Object> video = new LinkedHashMap<>();
        video.put("url", "/videos/project-" + projectId + ".mp4");
        video.put("duration", duration);
        video.put("createdAt", createdAt);
        return video;
    }

    private int renderVideoWithFfmpeg(Long userId, Long projectId, List<Map<String, Object>> orderedMedia, Path outputFile) {
        if (!Files.exists(Path.of(FFMPEG_EXECUTABLE))) {
            throw new IllegalStateException("FFmpeg executable not found at configured path");
        }

        Path tempDirectory = storageRoot.resolve("tmp").resolve("project-" + projectId);
        createDirectories(tempDirectory);
        Path concatFile = tempDirectory.resolve("concat.txt");

        int estimatedDuration = Math.max(orderedMedia.size() * 4, 4);

        try (BufferedWriter writer = Files.newBufferedWriter(concatFile)) {
            for (Map<String, Object> item : orderedMedia) {
                String type = String.valueOf(item.get("type"));
                String url = String.valueOf(item.get("url"));
                Path sourcePath = resolveMediaPathFromUrl(userId, projectId, url);
                Path preparedPath = prepareMediaForConcat(tempDirectory, sourcePath, type, ((Number) item.get("orderIndex")).intValue());

                writer.write("file '" + normalizeForFfmpeg(preparedPath) + "'");
                writer.newLine();

                if ("image".equalsIgnoreCase(type)) {
                    writer.write("duration 4");
                    writer.newLine();
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to prepare FFmpeg concat manifest", exception);
        }

        executeFfmpeg(List.of(
                FFMPEG_EXECUTABLE,
                "-y",
                "-f", "concat",
                "-safe", "0",
                "-i", concatFile.toAbsolutePath().toString(),
                "-vsync", "vfr",
                "-pix_fmt", "yuv420p",
                "-c:v", "libx264",
                "-movflags", "+faststart",
                outputFile.toAbsolutePath().toString()
        ));

        if (!Files.exists(outputFile) || isLikelyEmpty(outputFile)) {
            throw new IllegalStateException("FFmpeg did not produce a playable MP4 output");
        }

        return estimatedDuration;
    }

    private Path prepareMediaForConcat(Path tempDirectory, Path sourcePath, String type, int orderIndex) {
        String normalizedType = type == null ? "" : type.toLowerCase(Locale.ROOT);
        Path preparedPath = tempDirectory.resolve(String.format("%03d-%s.mp4", orderIndex, normalizedType));

        if ("image".equals(normalizedType)) {
            executeFfmpeg(List.of(
                    FFMPEG_EXECUTABLE,
                    "-y",
                    "-loop", "1",
                    "-t", "4",
                    "-i", sourcePath.toAbsolutePath().toString(),
                    "-vf", "scale=1280:720:force_original_aspect_ratio=decrease,pad=1280:720:(ow-iw)/2:(oh-ih)/2,format=yuv420p",
                    "-r", "30",
                    "-an",
                    "-c:v", "libx264",
                    preparedPath.toAbsolutePath().toString()
            ));
            return preparedPath;
        }

        if ("video".equals(normalizedType)) {
            executeFfmpeg(List.of(
                    FFMPEG_EXECUTABLE,
                    "-y",
                    "-i", sourcePath.toAbsolutePath().toString(),
                    "-vf", "scale=1280:720:force_original_aspect_ratio=decrease,pad=1280:720:(ow-iw)/2:(oh-ih)/2,format=yuv420p",
                    "-r", "30",
                    "-an",
                    "-c:v", "libx264",
                    preparedPath.toAbsolutePath().toString()
            ));
            return preparedPath;
        }

        throw new IllegalArgumentException("Unsupported media type for generation: " + type);
    }

    private void executeFfmpeg(List<String> command) {
        try {
            Process process = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .start();
            String ffmpegOutput = new String(process.getInputStream().readAllBytes());
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IllegalStateException("FFmpeg failed with exit code " + exitCode + ": " + ffmpegOutput);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to start FFmpeg process", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("FFmpeg process was interrupted", exception);
        }
    }

    private Path resolveMediaPathFromUrl(Long userId, Long projectId, String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        return resolveProjectMediaPath(userId, projectId, fileName);
    }

    private String normalizeForFfmpeg(Path path) {
        return path.toAbsolutePath().toString().replace("\\", "/").replace("'", "\\'");
    }

    private boolean isLikelyEmpty(Path filePath) {
        try {
            return Files.size(filePath) < 1024;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to inspect generated video output", exception);
        }
    }

    private String createPermanentSlug(Long projectId) {
        return ("m" + projectId + UUID.randomUUID().toString().replace("-", "").substring(0, 7)).toLowerCase();
    }

    private Map<String, Object> createPublicLink(String slug, String url) {
        Map<String, Object> link = new LinkedHashMap<>();
        link.put("slug", slug);
        link.put("url", url);
        return link;
    }

    private Map<String, Object> createQrCode(String publicUrl) {
        Map<String, Object> qr = new LinkedHashMap<>();
        qr.put("url", "https://api.qrserver.com/v1/create-qr-code/?size=512x512&data=" + publicUrl);
        qr.put("publicUrl", publicUrl);
        return qr;
    }
}

// Made with Bob
