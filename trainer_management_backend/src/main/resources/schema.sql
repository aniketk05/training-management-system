DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS trainer_availability;
DROP TABLE IF EXISTS trainees;
DROP TABLE IF EXISTS batches;
DROP TABLE IF EXISTS trainers;

CREATE TABLE trainers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL
);

CREATE TABLE batches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course VARCHAR(100) NOT NULL,
    trainer_id BIGINT NOT NULL,
    location VARCHAR(100) NOT NULL,
    start_date DATE,
    end_date DATE,
    FOREIGN KEY (trainer_id) REFERENCES trainers(id)
);

CREATE TABLE trainees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    batch_id BIGINT,
    FOREIGN KEY (batch_id) REFERENCES batches(id)
);

CREATE TABLE attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainee_id BIGINT NOT NULL,
    batch_id BIGINT NOT NULL,
    date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (trainee_id) REFERENCES trainees(id),
    FOREIGN KEY (batch_id) REFERENCES batches(id),
    UNIQUE KEY unique_attendance (trainee_id, batch_id, date)
);

CREATE TABLE trainer_availability (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainer_id BIGINT NOT NULL,
    available_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_booked BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (trainer_id) REFERENCES trainers(id)
);

CREATE INDEX idx_trainer_location ON trainers(location);
CREATE INDEX idx_batch_trainer ON batches(trainer_id);
CREATE INDEX idx_batch_location ON batches(location);
CREATE INDEX idx_trainee_batch ON trainees(batch_id);
CREATE INDEX idx_attendance_trainee ON attendance(trainee_id);
CREATE INDEX idx_attendance_batch ON attendance(batch_id);
CREATE INDEX idx_attendance_date ON attendance(date);
CREATE INDEX idx_availability_trainer ON trainer_availability(trainer_id);
CREATE INDEX idx_availability_date ON trainer_availability(available_date);