-- Create enum types for project
CREATE TYPE occasion_type AS ENUM (
    'BIRTHDAY',
    'ANNIVERSARY',
    'MEMORIAL',
    'WEDDING',
    'GRADUATION',
    'FRIENDSHIP',
    'TRAVEL',
    'CUSTOM'
);

CREATE TYPE project_status AS ENUM (
    'DRAFT',
    'PROCESSING',
    'COMPLETED',
    'PUBLISHED',
    'FAILED'
);

CREATE TYPE privacy_type AS ENUM (
    'PUBLIC',
    'FRIENDS',
    'PRIVATE'
);

-- Create projects table
CREATE TABLE IF NOT EXISTS projects (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    type occasion_type NOT NULL,
    status project_status NOT NULL DEFAULT 'DRAFT',
    privacy privacy_type NOT NULL DEFAULT 'PUBLIC',
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    published_at TIMESTAMP,
    CONSTRAINT fk_projects_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_projects_user_id ON projects(user_id);
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_projects_type ON projects(type);
CREATE INDEX idx_projects_privacy ON projects(privacy);
CREATE INDEX idx_projects_created_at ON projects(created_at DESC);
CREATE INDEX idx_projects_published_at ON projects(published_at DESC);
CREATE INDEX idx_projects_user_status ON projects(user_id, status);

-- Create GIN index for JSONB metadata column for faster JSON queries
CREATE INDEX idx_projects_metadata ON projects USING GIN (metadata);

-- Add comments for documentation
COMMENT ON TABLE projects IS 'Stores user projects (memory reels)';
COMMENT ON COLUMN projects.id IS 'Primary key, auto-incrementing project ID';
COMMENT ON COLUMN projects.user_id IS 'Foreign key to users table';
COMMENT ON COLUMN projects.title IS 'Project title';
COMMENT ON COLUMN projects.type IS 'Occasion type (birthday, memorial, etc.)';
COMMENT ON COLUMN projects.status IS 'Current project status in workflow';
COMMENT ON COLUMN projects.privacy IS 'Privacy setting for published projects';
COMMENT ON COLUMN projects.metadata IS 'JSON metadata specific to occasion type';
COMMENT ON COLUMN projects.created_at IS 'Timestamp when project was created';
COMMENT ON COLUMN projects.updated_at IS 'Timestamp when project was last updated';
COMMENT ON COLUMN projects.published_at IS 'Timestamp when project was published';

-- Made with Bob
