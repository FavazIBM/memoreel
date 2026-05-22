-- Create videos table
CREATE TABLE IF NOT EXISTS videos (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    url VARCHAR(500) NOT NULL,
    duration INTEGER NOT NULL,
    resolution VARCHAR(20) NOT NULL,
    size BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_videos_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_videos_project_id ON videos(project_id);
CREATE INDEX idx_videos_created_at ON videos(created_at DESC);

-- Create unique constraint to ensure one video per project
CREATE UNIQUE INDEX idx_videos_project_unique ON videos(project_id);

-- Add comments for documentation
COMMENT ON TABLE videos IS 'Stores generated video files for projects';
COMMENT ON COLUMN videos.id IS 'Primary key, auto-incrementing video ID';
COMMENT ON COLUMN videos.project_id IS 'Foreign key to projects table (one-to-one)';
COMMENT ON COLUMN videos.url IS 'S3 URL to the generated video file';
COMMENT ON COLUMN videos.duration IS 'Video duration in seconds';
COMMENT ON COLUMN videos.resolution IS 'Video resolution (e.g., 1920x1080)';
COMMENT ON COLUMN videos.size IS 'File size in bytes';
COMMENT ON COLUMN videos.created_at IS 'Timestamp when video was generated';

-- Made with Bob
