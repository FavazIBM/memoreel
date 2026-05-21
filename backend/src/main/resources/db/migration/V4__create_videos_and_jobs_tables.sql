-- Memoreel Database Migration V4
-- Create videos and video_jobs tables

-- Create videos table
CREATE TABLE IF NOT EXISTS videos (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL UNIQUE,
    url VARCHAR(500) NOT NULL,
    thumbnail_url VARCHAR(500),
    duration INTEGER NOT NULL,
    resolution VARCHAR(20) DEFAULT '1080p',
    size BIGINT NOT NULL,
    format VARCHAR(10) DEFAULT 'mp4',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_videos_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Create indexes for videos
CREATE INDEX idx_videos_project_id ON videos(project_id);
CREATE INDEX idx_videos_created_at ON videos(created_at DESC);

-- Create video_jobs table
CREATE TABLE IF NOT EXISTS video_jobs (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'QUEUED',
    progress INTEGER,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estimated_time INTEGER,
    CONSTRAINT fk_video_jobs_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Create indexes for video_jobs
CREATE INDEX idx_video_jobs_project_id ON video_jobs(project_id);
CREATE INDEX idx_video_jobs_status ON video_jobs(status);
CREATE INDEX idx_video_jobs_created_at ON video_jobs(created_at DESC);

-- Add check constraint for video job status
ALTER TABLE video_jobs ADD CONSTRAINT chk_video_jobs_status 
    CHECK (status IN ('QUEUED', 'PROCESSING', 'COMPLETED', 'FAILED'));

-- Made with Bob
