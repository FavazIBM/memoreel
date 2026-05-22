-- Create public_links table
CREATE TABLE IF NOT EXISTS public_links (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_public_links_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_public_links_project_id ON public_links(project_id);
CREATE INDEX idx_public_links_slug ON public_links(slug);
CREATE INDEX idx_public_links_created_at ON public_links(created_at DESC);

-- Create unique constraint to ensure one public link per project
CREATE UNIQUE INDEX idx_public_links_project_unique ON public_links(project_id);

-- Add comments for documentation
COMMENT ON TABLE public_links IS 'Stores public sharing links for published projects';
COMMENT ON COLUMN public_links.id IS 'Primary key, auto-incrementing link ID';
COMMENT ON COLUMN public_links.project_id IS 'Foreign key to projects table (one-to-one)';
COMMENT ON COLUMN public_links.slug IS 'Unique URL slug for public access';
COMMENT ON COLUMN public_links.created_at IS 'Timestamp when public link was created';

-- Made with Bob
