-- =========================
-- AUTH MODULE TABLES
-- =========================

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,

    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

-- =========================
-- HALL MODULE TABLES
-- =========================

CREATE TABLE seat_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE halls (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE seats (
    id BIGSERIAL PRIMARY KEY,

    type_id BIGINT NOT NULL,
    hall_id BIGINT NOT NULL,

    row_number INTEGER NOT NULL,
    seat_number INTEGER NOT NULL,

    CONSTRAINT fk_seat_type
        FOREIGN KEY (type_id)
        REFERENCES seat_types (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_seat_hall
        FOREIGN KEY (hall_id)
        REFERENCES halls (id)
        ON DELETE CASCADE,

    CONSTRAINT uk_seat_position
        UNIQUE (hall_id, row_number, seat_number)
);

CREATE INDEX idx_seats_hall_id ON seats(hall_id);
CREATE INDEX idx_seats_type_id ON seats(type_id);

-- =========================
-- MOVIE MODULE TABLES
-- =========================

CREATE TABLE genres (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE movies (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    duration BIGINT NOT NULL,
    description TEXT NOT NULL,
    cover_image_link VARCHAR(512) NOT NULL
);

CREATE TABLE movies_genres (
    movie_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,

    PRIMARY KEY (movie_id, genre_id),

    CONSTRAINT fk_movies_genres_movie
        FOREIGN KEY (movie_id)
        REFERENCES movies (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_movies_genres_genre
        FOREIGN KEY (genre_id)
        REFERENCES genres (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_movies_genres_movie_id ON movies_genres(movie_id);
CREATE INDEX idx_movies_genres_genre_id ON movies_genres(genre_id);

-- =========================
-- SCHEDULE MODULE TABLES
-- =========================

CREATE TABLE schedules (
    id BIGSERIAL PRIMARY KEY,

    movie_id BIGINT NOT NULL,
    hall_id BIGINT NOT NULL,

    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,

    CONSTRAINT fk_schedules_movie
        FOREIGN KEY (movie_id)
        REFERENCES movies (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_schedules_hall
        FOREIGN KEY (hall_id)
        REFERENCES halls (id)
        ON DELETE RESTRICT
);

CREATE TABLE schedule_seats (
    id BIGSERIAL PRIMARY KEY,

    seat_id BIGINT NOT NULL,
    schedule_id BIGINT NOT NULL,

    price NUMERIC(10, 2) NOT NULL,

    CONSTRAINT fk_schedule_seats_schedule
        FOREIGN KEY (schedule_id)
        REFERENCES schedules (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_schedule_seats_seat
        FOREIGN KEY (seat_id)
        REFERENCES seats (id)
        ON DELETE RESTRICT,

    CONSTRAINT uk_schedule_seat_unique
        UNIQUE (schedule_id, seat_id)
);

CREATE INDEX idx_schedules_movie_id ON schedules(movie_id);
CREATE INDEX idx_schedules_hall_id ON schedules(hall_id);

CREATE INDEX idx_schedule_seats_schedule_id ON schedule_seats(schedule_id);
CREATE INDEX idx_schedule_seats_seat_id ON schedule_seats(seat_id);

-- =========================
-- BOOKING MODULE TABLES
-- =========================

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,

    schedule_id BIGINT NOT NULL,

    CONSTRAINT fk_bookings_schedule
        FOREIGN KEY (schedule_id)
        REFERENCES schedules (id)
        ON DELETE RESTRICT
);

CREATE TABLE seat_reservations (
    id BIGSERIAL PRIMARY KEY,

    schedule_seat_id BIGINT NOT NULL UNIQUE,
    booking_id BIGINT NOT NULL,

    CONSTRAINT fk_seat_reservations_booking
        FOREIGN KEY (booking_id)
        REFERENCES bookings (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_seat_reservations_schedule_seat
        FOREIGN KEY (schedule_seat_id)
        REFERENCES schedule_seats (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_bookings_schedule_id ON bookings(schedule_id);
CREATE INDEX idx_seat_reservations_booking_id ON seat_reservations(booking_id);

-- =========================
-- PAYMENT MODULE TABLES
-- =========================

CREATE TABLE intents (
    id BIGSERIAL PRIMARY KEY,

    gateway_intent_id VARCHAR(255) NOT NULL UNIQUE,
    price NUMERIC(10, 2) NOT NULL
);

CREATE TABLE completions (
    id BIGSERIAL PRIMARY KEY,

    intent_id BIGINT NOT NULL UNIQUE,
    gateway_completion_id VARCHAR(255) UNIQUE,

    CONSTRAINT fk_completions_intent
        FOREIGN KEY (intent_id)
        REFERENCES intents (id)
        ON DELETE RESTRICT
);

CREATE TABLE refunds (
    id BIGSERIAL PRIMARY KEY,

    completion_id BIGINT NOT NULL UNIQUE,

    CONSTRAINT fk_refunds_completion
        FOREIGN KEY (completion_id)
        REFERENCES completions (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_completions_intent_id ON completions(intent_id);
CREATE INDEX idx_refunds_completion_id ON refunds(completion_id);

-- =========================
-- ORDER MODULE TABLES
-- =========================

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,

    intent_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    booking_id BIGINT NOT NULL,

    CONSTRAINT fk_orders_intent
        FOREIGN KEY (intent_id)
        REFERENCES intents (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_orders_booking
        FOREIGN KEY (booking_id)
        REFERENCES bookings (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_orders_intent_id ON orders(intent_id);
CREATE INDEX idx_orders_booking_id ON orders(booking_id);
CREATE INDEX idx_orders_user_id ON orders(user_id);