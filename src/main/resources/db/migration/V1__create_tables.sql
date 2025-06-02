CREATE TABLE users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE halls (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    status VARCHAR(255) NOT NULL
);

CREATE TABLE movies (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title VARCHAR(255) NOT NULL UNIQUE,
    duration INT NOT NULL,
    description TEXT NOT NULL,
    cover VARCHAR(255) NOT NULL
);

CREATE TABLE seats (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    type VARCHAR(255) NOT NULL,
    row_number INT NOT NULL,
    seat_number INT NOT NULL,
    hall_id BIGINT NOT NULL,
    CONSTRAINT fk_seat_hall FOREIGN KEY (hall_id) REFERENCES halls(id),
    CONSTRAINT uc_seat UNIQUE (hall_id, row_number, seat_number)
);

CREATE TABLE schedules (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    movie_id BIGINT NOT NULL,
    hall_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_schedule_movie FOREIGN KEY (movie_id) REFERENCES movies(id),
    CONSTRAINT fk_schedule_hall FOREIGN KEY (hall_id) REFERENCES halls(id)
);

CREATE INDEX idx_schedule_hall_time ON schedules(hall_id, start_time, end_time);
CREATE INDEX idx_schedule_movie_time ON schedules(movie_id, start_time, end_time);

CREATE TABLE payments (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    order_id VARCHAR(255) NOT NULL UNIQUE,
    capture_id VARCHAR(255) UNIQUE,
    price DECIMAL(10, 2) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_payment_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_capture_id_created_at ON payments(capture_id, created_at);

CREATE TABLE bookings (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    seat_id BIGINT NOT NULL,
    schedule_id BIGINT NOT NULL,
    payment_id BIGINT NOT NULL,
    CONSTRAINT fk_booking_seat FOREIGN KEY (seat_id) REFERENCES seats(id),
    CONSTRAINT fk_booking_schedule FOREIGN KEY (schedule_id) REFERENCES schedules(id),
    CONSTRAINT fk_booking_payment FOREIGN KEY (payment_id) REFERENCES payments(id),
    CONSTRAINT uc_booking UNIQUE (schedule_id, seat_id)
);