CREATE TABLE sleep_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    sleep_date DATE NOT NULL,
    bed_time TIME NOT NULL,
    wake_time TIME NOT NULL,
    minutes_in_bed INTEGER NOT NULL,
    sleep_status VARCHAR(5) NOT NULL CHECK (sleep_status IN ('BAD', 'OK', 'GOOD')),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT unique_user_sleep_date UNIQUE (user_id, sleep_date)
);