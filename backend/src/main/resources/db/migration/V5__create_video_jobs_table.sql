-- Create enum type for video job status
CREATE TYPE video_job_status AS ENUM (
    'PENDING',
    'PROCESSING',
    'COMPLETED',
    'FAILED'
);

-- Create video_jobs table
CREATE TABLE IF NOT EXISTS video_jobs (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    status video_job_status NOT NULL DEFAULT 'PENDING',
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    error_message TEXT,
    retry_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_video_jobs_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_video_jobs_project_id ON video_jobs(project_id);
CREATE INDEX idx_video_jobs_status ON video_jobs(status);
CREATE INDEX idx_video_jobs_created_at ON video_jobs(created_at DESC);
CREATE INDEX idx_video_jobs_status_created ON video_jobs(status, created_at);

-- Add comments for documentation
COMMENT ON TABLE video_jobs IS 'Stores video generation job queue and status';
COMMENT ON COLUMN video_jobs.id IS 'Primary key, auto-incrementing job ID';
COMMENT ON COLUMN video_jobs.project_id IS 'Foreign key to projects table';
COMMENT ON COLUMN video_jobs.status IS 'Current job status (PENDING, PROCESSING, COMPLETED, FAILED)';
COMMENT ON COLUMN video_jobs.started_at IS 'Timestamp when job processing started';
COMMENT ON COLUMN video_jobs.completed_at IS 'Timestamp when job completed (success or failure)';
COMMENT ON COLUMN video_jobs.error_message IS 'Error message if job failed';
COMMENT ON COLUMN video_jobs.retry_count IS 'Number of retry attempts';
COMMENT ON COLUMN video_jobs.created_at IS 'Timestamp when job was created';
COMMENT ON COLUMN video_jobs.updated_at IS 'Timestamp when job was last updated';

-- Made with Bob
