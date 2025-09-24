-- Create database if not exists
CREATE DATABASE IF NOT EXISTS training_db;
USE training_db;

-- Set character set
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS trainer_availability;
DROP TABLE IF EXISTS trainees;
DROP TABLE IF EXISTS batches;
DROP TABLE IF EXISTS trainers;

-- Create trainers table
CREATE TABLE trainers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_trainer_location (location)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create batches table
CREATE TABLE batches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course VARCHAR(100) NOT NULL,
    trainer_id BIGINT NOT NULL,
    location VARCHAR(100) NOT NULL,
    start_date DATE,
    end_date DATE,
    max_capacity INT DEFAULT 30,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (trainer_id) REFERENCES trainers(id) ON DELETE CASCADE,
    INDEX idx_batch_trainer (trainer_id),
    INDEX idx_batch_location (location),
    INDEX idx_batch_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create trainees table
CREATE TABLE trainees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    batch_id BIGINT,
    enrollment_date DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (batch_id) REFERENCES batches(id) ON DELETE SET NULL,
    INDEX idx_trainee_batch (batch_id),
    INDEX idx_trainee_email (email),
    INDEX idx_trainee_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create attendance table
CREATE TABLE attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainee_id BIGINT NOT NULL,
    batch_id BIGINT NOT NULL,
    date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    remarks TEXT,
    marked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    marked_by VARCHAR(100),
    FOREIGN KEY (trainee_id) REFERENCES trainees(id) ON DELETE CASCADE,
    FOREIGN KEY (batch_id) REFERENCES batches(id) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (trainee_id, batch_id, date),
    INDEX idx_attendance_trainee (trainee_id),
    INDEX idx_attendance_batch (batch_id),
    INDEX idx_attendance_date (date),
    INDEX idx_attendance_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create trainer_availability table
CREATE TABLE trainer_availability (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainer_id BIGINT NOT NULL,
    available_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_booked BOOLEAN DEFAULT FALSE,
    booking_reference VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (trainer_id) REFERENCES trainers(id) ON DELETE CASCADE,
    INDEX idx_availability_trainer (trainer_id),
    INDEX idx_availability_date (available_date),
    INDEX idx_availability_booked (is_booked)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample trainers
INSERT INTO trainers (name, location) VALUES
('Dr. John Smith', 'New York'),
('Prof. Sarah Johnson', 'New York'),
('Michael Brown', 'Los Angeles'),
('Emily Davis', 'Chicago'),
('Robert Wilson', 'Chicago'),
('Lisa Anderson', 'San Francisco'),
('James Taylor', 'Boston'),
('Maria Garcia', 'Miami');

-- Insert sample batches
INSERT INTO batches (course, trainer_id, location, start_date, end_date, max_capacity, status) VALUES
('Java Programming Fundamentals', 1, 'New York', '2024-01-15', '2024-03-15', 25, 'ACTIVE'),
('Advanced Spring Boot', 1, 'New York', '2024-02-01', '2024-04-01', 20, 'ACTIVE'),
('Python for Data Science', 2, 'New York', '2024-01-20', '2024-03-20', 30, 'ACTIVE'),
('Machine Learning Basics', 3, 'Los Angeles', '2024-01-10', '2024-03-10', 25, 'ACTIVE'),
('Full Stack Web Development', 4, 'Chicago', '2024-02-15', '2024-04-15', 20, 'ACTIVE'),
('DevOps and CI/CD', 5, 'Chicago', '2024-01-25', '2024-03-25', 15, 'ACTIVE'),
('React.js Masterclass', 6, 'San Francisco', '2024-02-10', '2024-04-10', 25, 'ACTIVE'),
('Cloud Computing with AWS', 7, 'Boston', '2024-01-30', '2024-03-30', 20, 'ACTIVE');

-- Insert sample trainees
INSERT INTO trainees (name, email, phone, batch_id, enrollment_date, status) VALUES
('Alice Cooper', 'alice.cooper@email.com', '555-0101', 1, '2024-01-10', 'ACTIVE'),
('Bob Martin', 'bob.martin@email.com', '555-0102', 1, '2024-01-11', 'ACTIVE'),
('Charlie Rose', 'charlie.rose@email.com', '555-0103', 1, '2024-01-12', 'ACTIVE'),
('Diana Prince', 'diana.prince@email.com', '555-0104', 2, '2024-01-28', 'ACTIVE'),
('Edward Norton', 'edward.norton@email.com', '555-0105', 2, '2024-01-29', 'ACTIVE'),
('Fiona Apple', 'fiona.apple@email.com', '555-0106', 3, '2024-01-15', 'ACTIVE'),
('George Lucas', 'george.lucas@email.com', '555-0107', 3, '2024-01-16', 'ACTIVE'),
('Helen Hunt', 'helen.hunt@email.com', '555-0108', 4, '2024-01-08', 'ACTIVE'),
('Ian McKellen', 'ian.mckellen@email.com', '555-0109', 4, '2024-01-09', 'ACTIVE'),
('Julia Roberts', 'julia.roberts@email.com', '555-0110', 5, '2024-02-10', 'ACTIVE'),
('Kevin Spacey', 'kevin.spacey@email.com', '555-0111', 5, '2024-02-11', 'ACTIVE'),
('Laura Dern', 'laura.dern@email.com', '555-0112', 6, '2024-01-20', 'ACTIVE'),
('Mark Wahlberg', 'mark.wahlberg@email.com', '555-0113', 7, '2024-02-05', 'ACTIVE'),
('Nancy Drew', 'nancy.drew@email.com', '555-0114', 7, '2024-02-06', 'ACTIVE'),
('Oscar Isaac', 'oscar.isaac@email.com', '555-0115', 8, '2024-01-25', 'ACTIVE');

-- Insert sample trainer availability
INSERT INTO trainer_availability (trainer_id, available_date, start_time, end_time, is_booked) VALUES
-- John Smith availability
(1, '2024-01-15', '09:00:00', '17:00:00', true),
(1, '2024-01-16', '09:00:00', '17:00:00', true),
(1, '2024-01-17', '09:00:00', '17:00:00', true),
(1, '2024-01-18', '09:00:00', '13:00:00', false),
(1, '2024-01-19', '14:00:00', '18:00:00', false),

-- Sarah Johnson availability
(2, '2024-01-20', '09:00:00', '17:00:00', true),
(2, '2024-01-21', '09:00:00', '17:00:00', true),
(2, '2024-01-22', '09:00:00', '17:00:00', false),
(2, '2024-01-23', '10:00:00', '16:00:00', false),

-- Michael Brown availability
(3, '2024-01-10', '09:00:00', '17:00:00', true),
(3, '2024-01-11', '09:00:00', '17:00:00', true),
(3, '2024-01-12', '09:00:00', '17:00:00', false),

-- Emily Davis availability
(4, '2024-02-15', '09:00:00', '17:00:00', true),
(4, '2024-02-16', '09:00:00', '17:00:00', true),
(4, '2024-02-17', '09:00:00', '17:00:00', false),

-- Robert Wilson availability
(5, '2024-01-25', '09:00:00', '17:00:00', true),
(5, '2024-01-26', '09:00:00', '17:00:00', false),
(5, '2024-01-27', '10:00:00', '15:00:00', false);

-- Insert sample attendance records
INSERT INTO attendance (trainee_id, batch_id, date, status, remarks) VALUES
-- Batch 1 attendance for Jan 15
(1, 1, '2024-01-15', 'PRESENT', 'On time'),
(2, 1, '2024-01-15', 'PRESENT', 'On time'),
(3, 1, '2024-01-15', 'ABSENT', 'Sick leave'),

-- Batch 1 attendance for Jan 16
(1, 1, '2024-01-16', 'PRESENT', 'On time'),
(2, 1, '2024-01-16', 'LATE', 'Arrived 15 minutes late'),
(3, 1, '2024-01-16', 'PRESENT', 'On time'),

-- Batch 1 attendance for Jan 17
(1, 1, '2024-01-17', 'PRESENT', 'On time'),
(2, 1, '2024-01-17', 'PRESENT', 'On time'),
(3, 1, '2024-01-17', 'PRESENT', 'On time'),

-- Batch 2 attendance for Feb 1
(4, 2, '2024-02-01', 'PRESENT', 'On time'),
(5, 2, '2024-02-01', 'PRESENT', 'On time'),

-- Batch 3 attendance for Jan 20
(6, 3, '2024-01-20', 'PRESENT', 'On time'),
(7, 3, '2024-01-20', 'ABSENT', 'Personal emergency'),

-- Batch 4 attendance for Jan 10
(8, 4, '2024-01-10', 'PRESENT', 'On time'),
(9, 4, '2024-01-10', 'LATE', 'Traffic delay'),

-- Batch 5 attendance for Feb 15
(10, 5, '2024-02-15', 'PRESENT', 'On time'),
(11, 5, '2024-02-15', 'PRESENT', 'On time');

-- Create views for reporting
CREATE OR REPLACE VIEW v_batch_summary AS
SELECT 
    b.id AS batch_id,
    b.course,
    b.location,
    t.name AS trainer_name,
    b.start_date,
    b.end_date,
    b.status,
    COUNT(DISTINCT tr.id) AS enrolled_count,
    b.max_capacity
FROM batches b
LEFT JOIN trainers t ON b.trainer_id = t.id
LEFT JOIN trainees tr ON tr.batch_id = b.id
GROUP BY b.id;

CREATE OR REPLACE VIEW v_attendance_summary AS
SELECT 
    b.id AS batch_id,
    b.course,
    a.date,
    COUNT(CASE WHEN a.status = 'PRESENT' THEN 1 END) AS present_count,
    COUNT(CASE WHEN a.status = 'ABSENT' THEN 1 END) AS absent_count,
    COUNT(CASE WHEN a.status = 'LATE' THEN 1 END) AS late_count,
    COUNT(*) AS total_marked
FROM attendance a
JOIN batches b ON a.batch_id = b.id
GROUP BY b.id, a.date;

-- Create stored procedures for common operations
DELIMITER //

CREATE PROCEDURE sp_get_trainer_schedule(IN trainer_id_param BIGINT)
BEGIN
    SELECT 
        ta.available_date,
        ta.start_time,
        ta.end_time,
        ta.is_booked,
        b.course AS booked_for_course
    FROM trainer_availability ta
    LEFT JOIN batches b ON ta.trainer_id = b.trainer_id 
        AND ta.available_date BETWEEN b.start_date AND b.end_date
        AND ta.is_booked = TRUE
    WHERE ta.trainer_id = trainer_id_param
    ORDER BY ta.available_date, ta.start_time;
END//

CREATE PROCEDURE sp_calculate_attendance_percentage(IN trainee_id_param BIGINT)
BEGIN
    SELECT 
        tr.name AS trainee_name,
        b.course,
        COUNT(*) AS total_sessions,
        SUM(CASE WHEN a.status IN ('PRESENT', 'LATE') THEN 1 ELSE 0 END) AS attended_sessions,
        ROUND((SUM(CASE WHEN a.status IN ('PRESENT', 'LATE') THEN 1 ELSE 0 END) * 100.0) / COUNT(*), 2) AS attendance_percentage
    FROM trainees tr
    JOIN attendance a ON tr.id = a.trainee_id
    JOIN batches b ON a.batch_id = b.id
    WHERE tr.id = trainee_id_param
    GROUP BY tr.id, b.id;
END//

DELIMITER ;

-- Grant permissions (adjust as needed for your setup)
-- GRANT ALL PRIVILEGES ON training_db.* TO 'training_user'@'%';
-- FLUSH PRIVILEGES;

-- Display summary
SELECT 'Database initialization completed!' AS message;
SELECT COUNT(*) AS trainer_count FROM trainers;
SELECT COUNT(*) AS batch_count FROM batches;
SELECT COUNT(*) AS trainee_count FROM trainees;
SELECT COUNT(*) AS attendance_count FROM attendance;
SELECT COUNT(*) AS availability_count FROM trainer_availability;