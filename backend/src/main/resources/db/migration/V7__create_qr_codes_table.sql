-- Create qr_codes table
CREATE TABLE IF NOT EXISTS qr_codes (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    qr_image_url VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_qr_codes_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_qr_codes_project_id ON qr_codes(project_id);
CREATE INDEX idx_qr_codes_created_at ON qr_codes(created_at DESC);

-- Create unique constraint to ensure one QR code per project
CREATE UNIQUE INDEX idx_qr_codes_project_unique ON qr_codes(project_id);

-- Add comments for documentation
COMMENT ON TABLE qr_codes IS 'Stores QR code images for published projects';
COMMENT ON COLUMN qr_codes.id IS 'Primary key, auto-incrementing QR code ID';
COMMENT ON COLUMN qr_codes.project_id IS 'Foreign key to projects table (one-to-one)';
COMMENT ON COLUMN qr_codes.qr_image_url IS 'S3 URL to the QR code image';
COMMENT ON COLUMN qr_codes.created_at IS 'Timestamp when QR code was generated';

-- Made with Bob
