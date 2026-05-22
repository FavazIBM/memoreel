-- Create enum type for media type
CREATE TYPE media_type AS ENUM (
    'IMAGE',
    'VIDEO'
);

-- Create media table
CREATE TABLE IF NOT EXISTS media (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    type media_type NOT NULL,
    url VARCHAR(500) NOT NULL,
    thumbnail_url VARCHAR(500),
    order_index INTEGER NOT NULL,
    size BIGINT NOT NULL,
    duration INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_media_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_media_project_id ON media(project_id);
CREATE INDEX idx_media_type ON media(type);
CREATE INDEX idx_media_order ON media(project_id, order_index);
CREATE INDEX idx_media_created_at ON media(created_at DESC);

-- Create unique constraint to prevent duplicate order_index within same project
CREATE UNIQUE INDEX idx_media_project_order ON media(project_id, order_index);

-- Add comments for documentation
COMMENT ON TABLE media IS 'Stores media files (images and videos) for projects';
COMMENT ON COLUMN media.id IS 'Primary key, auto-incrementing media ID';
COMMENT ON COLUMN media.project_id IS 'Foreign key to projects table';
COMMENT ON COLUMN media.type IS 'Media type (IMAGE or VIDEO)';
COMMENT ON COLUMN media.url IS 'S3 URL to the media file';
COMMENT ON COLUMN media.thumbnail_url IS 'S3 URL to the thumbnail (for videos)';
COMMENT ON COLUMN media.order_index IS 'Order of media in the project (0-based)';
COMMENT ON COLUMN media.size IS 'File size in bytes';
COMMENT ON COLUMN media.duration IS 'Duration in seconds (for videos only)';
COMMENT ON COLUMN media.created_at IS 'Timestamp when media was uploaded';

-- Made with Bob
