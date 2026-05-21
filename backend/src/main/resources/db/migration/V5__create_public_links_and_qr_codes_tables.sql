-- Memoreel Database Migration V5
-- Create public_links and qr_codes tables

-- Create public_links table
CREATE TABLE IF NOT EXISTS public_links (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL UNIQUE,
    slug VARCHAR(12) NOT NULL UNIQUE,
    public_url VARCHAR(500) NOT NULL,
    views BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_viewed_at TIMESTAMP,
    CONSTRAINT fk_public_links_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Create indexes for public_links
CREATE INDEX idx_public_links_project_id ON public_links(project_id);
CREATE INDEX idx_public_links_slug ON public_links(slug);
CREATE INDEX idx_public_links_created_at ON public_links(created_at DESC);

-- Create qr_codes table
CREATE TABLE IF NOT EXISTS qr_codes (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL UNIQUE,
    image_url VARCHAR(500) NOT NULL,
    target_url VARCHAR(500) NOT NULL,
    size INTEGER NOT NULL DEFAULT 512,
    format VARCHAR(10) DEFAULT 'PNG',
    error_correction VARCHAR(1) DEFAULT 'H',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    regenerated_at TIMESTAMP,
    CONSTRAINT fk_qr_codes_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Create indexes for qr_codes
CREATE INDEX idx_qr_codes_project_id ON qr_codes(project_id);
CREATE INDEX idx_qr_codes_created_at ON qr_codes(created_at DESC);

-- Made with Bob
