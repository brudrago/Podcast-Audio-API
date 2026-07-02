CREATE TABLE episodes (
  id UUID PRIMARY KEY,
  title TEXT NOT NULL,
  description TEXT,
  duration_seconds INTEGER,
  audio_key TEXT,
  published_at TIMESTAMP,
  status TEXT NOT NULL DEFAULT 'draft',
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);