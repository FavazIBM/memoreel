-- Memoreel Database Migration V1
-- Create users table

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(500),
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    provider VARCHAR(50),
    provider_id VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP
);

-- Create index on email for faster lookups
CREATE INDEX idx_users_email ON users(email);

-- Create index on provider_id for OAuth lookups
CREATE INDEX idx_users_provider_id ON users(provider_id);

-- Create index on active status
CREATE INDEX idx_users_active ON users(active);

-- Made with Bob
