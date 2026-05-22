-- Create templates table
CREATE TABLE templates (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    occasion_type occasion_type NOT NULL,
    mood VARCHAR(50) NOT NULL,
    duration_per_image INTEGER NOT NULL DEFAULT 3,
    transition_type VARCHAR(50) NOT NULL DEFAULT 'fade',
    music_track_url TEXT,
    text_style TEXT,
    config JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for efficient template lookup
CREATE INDEX idx_templates_occasion ON templates(occasion_type);
CREATE INDEX idx_templates_occasion_mood ON templates(occasion_type, mood);

-- Insert default templates for each occasion type
INSERT INTO templates (name, occasion_type, mood, duration_per_image, transition_type, text_style, config) VALUES
-- Birthday templates
('Birthday Vibrant', 'BIRTHDAY', 'vibrant', 3, 'fade', 
 '{"font": "Arial", "color": "#FF6B6B", "size": 48, "backgroundColor": "rgba(0,0,0,0.5)"}',
 '{"titleDuration": 3, "creditsDuration": 3, "fadeInDuration": 1, "fadeOutDuration": 1}'::jsonb),

('Birthday Playful', 'BIRTHDAY', 'playful', 3, 'slide', 
 '{"font": "Comic Sans MS", "color": "#FFD700", "size": 52, "backgroundColor": "rgba(255,105,180,0.5)"}',
 '{"titleDuration": 3, "creditsDuration": 3, "fadeInDuration": 1, "fadeOutDuration": 1}'::jsonb),

-- Memorial templates
('Memorial Calm', 'MEMORIAL', 'calm', 4, 'fade', 
 '{"font": "Georgia", "color": "#FFFFFF", "size": 36, "backgroundColor": "rgba(0,0,0,0.7)"}',
 '{"titleDuration": 4, "creditsDuration": 4, "fadeInDuration": 2, "fadeOutDuration": 2}'::jsonb),

('Memorial Elegant', 'MEMORIAL', 'elegant', 4, 'fade', 
 '{"font": "Times New Roman", "color": "#E8E8E8", "size": 38, "backgroundColor": "rgba(0,0,0,0.8)"}',
 '{"titleDuration": 4, "creditsDuration": 4, "fadeInDuration": 2, "fadeOutDuration": 2}'::jsonb),

-- Wedding templates
('Wedding Elegant', 'WEDDING', 'elegant', 3, 'slide', 
 '{"font": "Playfair Display", "color": "#FFD700", "size": 42, "backgroundColor": "rgba(255,255,255,0.3)"}',
 '{"titleDuration": 4, "creditsDuration": 4, "fadeInDuration": 1.5, "fadeOutDuration": 1.5}'::jsonb),

('Wedding Romantic', 'WEDDING', 'romantic', 3, 'zoom', 
 '{"font": "Great Vibes", "color": "#FFB6C1", "size": 48, "backgroundColor": "rgba(255,255,255,0.4)"}',
 '{"titleDuration": 4, "creditsDuration": 4, "fadeInDuration": 1.5, "fadeOutDuration": 1.5}'::jsonb),

-- Anniversary templates
('Anniversary Romantic', 'ANNIVERSARY', 'romantic', 3, 'zoom', 
 '{"font": "Brush Script MT", "color": "#FF69B4", "size": 40, "backgroundColor": "rgba(0,0,0,0.5)"}',
 '{"titleDuration": 3, "creditsDuration": 3, "fadeInDuration": 1, "fadeOutDuration": 1}'::jsonb),

('Anniversary Elegant', 'ANNIVERSARY', 'elegant', 3, 'fade', 
 '{"font": "Palatino", "color": "#DAA520", "size": 44, "backgroundColor": "rgba(0,0,0,0.6)"}',
 '{"titleDuration": 3, "creditsDuration": 3, "fadeInDuration": 1, "fadeOutDuration": 1}'::jsonb),

-- Graduation templates
('Graduation Vibrant', 'GRADUATION', 'vibrant', 3, 'slide', 
 '{"font": "Arial Black", "color": "#4169E1", "size": 46, "backgroundColor": "rgba(255,215,0,0.5)"}',
 '{"titleDuration": 3, "creditsDuration": 3, "fadeInDuration": 1, "fadeOutDuration": 1}'::jsonb),

('Graduation Elegant', 'GRADUATION', 'elegant', 3, 'fade', 
 '{"font": "Garamond", "color": "#000080", "size": 42, "backgroundColor": "rgba(255,255,255,0.6)"}',
 '{"titleDuration": 3, "creditsDuration": 3, "fadeInDuration": 1, "fadeOutDuration": 1}'::jsonb),

-- Baby templates
('Baby Playful', 'BABY', 'playful', 3, 'zoom', 
 '{"font": "Comic Sans MS", "color": "#87CEEB", "size": 44, "backgroundColor": "rgba(255,192,203,0.5)"}',
 '{"titleDuration": 3, "creditsDuration": 3, "fadeInDuration": 1, "fadeOutDuration": 1}'::jsonb),

('Baby Calm', 'BABY', 'calm', 3, 'fade', 
 '{"font": "Verdana", "color": "#FFB6C1", "size": 40, "backgroundColor": "rgba(173,216,230,0.5)"}',
 '{"titleDuration": 3, "creditsDuration": 3, "fadeInDuration": 1, "fadeOutDuration": 1}'::jsonb),

-- Retirement templates
('Retirement Elegant', 'RETIREMENT', 'elegant', 3, 'fade', 
 '{"font": "Georgia", "color": "#B8860B", "size": 40, "backgroundColor": "rgba(0,0,0,0.6)"}',
 '{"titleDuration": 3, "creditsDuration": 3, "fadeInDuration": 1.5, "fadeOutDuration": 1.5}'::jsonb),

('Retirement Calm', 'RETIREMENT', 'calm', 4, 'fade', 
 '{"font": "Times New Roman", "color": "#CD853F", "size": 38, "backgroundColor": "rgba(0,0,0,0.5)"}',
 '{"titleDuration": 3, "creditsDuration": 3, "fadeInDuration": 1.5, "fadeOutDuration": 1.5}'::jsonb),

-- Other/Generic templates
('Generic Vibrant', 'OTHER', 'vibrant', 3, 'slide', 
 '{"font": "Arial", "color": "#FF6347", "size": 44, "backgroundColor": "rgba(0,0,0,0.5)"}',
 '{"titleDuration": 3, "creditsDuration": 3, "fadeInDuration": 1, "fadeOutDuration": 1}'::jsonb),

('Generic Calm', 'OTHER', 'calm', 3, 'fade', 
 '{"font": "Verdana", "color": "#FFFFFF", "size": 40, "backgroundColor": "rgba(0,0,0,0.6)"}',
 '{"titleDuration": 3, "creditsDuration": 3, "fadeInDuration": 1, "fadeOutDuration": 1}'::jsonb);

-- Add comment to table
COMMENT ON TABLE templates IS 'Video templates with styling and configuration for different occasions';
COMMENT ON COLUMN templates.mood IS 'Template mood: vibrant, calm, elegant, playful, romantic';
COMMENT ON COLUMN templates.transition_type IS 'Transition effect: fade, slide, zoom';
COMMENT ON COLUMN templates.text_style IS 'JSON configuration for text overlay styling';
COMMENT ON COLUMN templates.config IS 'Additional template configuration in JSONB format';

-- Made with Bob
