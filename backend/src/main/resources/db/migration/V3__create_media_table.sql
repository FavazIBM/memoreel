-- Memoreel Database Migration V3
-- Create media table

CREATE TABLE IF NOT EXISTS media (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    url VARCHAR(500) NOT NULL,
    thumbnail_url VARCHAR(500),
    order_index INTEGER NOT NULL,
    size BIGINT NOT NULL,
    mime_type VARCHAR(100),
    original_filename VARCHAR(255),
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_media_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Create indexes for faster queries
CREATE INDEX idx_media_project_id ON media(project_id);
CREATE INDEX idx_media_order_index ON media(project_id, order_index);
CREATE INDEX idx_media_type ON media(type);

-- Add unique constraint for order_index per project
CREATE UNIQUE INDEX idx_media_project_order ON media(project_id, order_index);

-- Add check constraint for media type
ALTER TABLE media ADD CONSTRAINT chk_media_type 
    CHECK (type IN ('IMAGE', 'VIDEO'));

-- Made with Bob
