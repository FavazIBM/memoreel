-- Memoreel Database Migration V2
-- Create projects table

CREATE TABLE IF NOT EXISTS projects (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    privacy VARCHAR(20) DEFAULT 'PUBLIC',
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    published_at TIMESTAMP,
    CONSTRAINT fk_projects_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for faster queries
CREATE INDEX idx_projects_user_id ON projects(user_id);
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_projects_type ON projects(type);
CREATE INDEX idx_projects_created_at ON projects(created_at DESC);
CREATE INDEX idx_projects_user_status ON projects(user_id, status);

-- Add check constraint for status values
ALTER TABLE projects ADD CONSTRAINT chk_projects_status 
    CHECK (status IN ('DRAFT', 'PROCESSING', 'COMPLETED', 'PUBLISHED', 'FAILED'));

-- Add check constraint for privacy values
ALTER TABLE projects ADD CONSTRAINT chk_projects_privacy 
    CHECK (privacy IN ('PUBLIC', 'FRIENDS', 'PRIVATE'));

-- Made with Bob
