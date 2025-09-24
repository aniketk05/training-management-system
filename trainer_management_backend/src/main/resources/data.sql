-- Insert Trainers
INSERT INTO trainers (name, location) VALUES
('John Smith', 'New York'),
('Sarah Johnson', 'New York'),
('Michael Brown', 'Los Angeles'),
('Emily Davis', 'Chicago'),
('Robert Wilson', 'Chicago');

-- Insert Batches
INSERT INTO batches (course, trainer_id, location, start_date, end_date) VALUES
('Java Programming', 1, 'New York', '2024-01-15', '2024-03-15'),
('Spring Boot', 1, 'New York', '2024-02-01', '2024-04-01'),
('Python Basics', 2, 'New York', '2024-01-20', '2024-03-20'),
('Data Science', 3, 'Los Angeles', '2024-01-10', '2024-03-10'),
('Web Development', 4, 'Chicago', '2024-02-15', '2024-04-15');

-- Insert Trainees
INSERT INTO trainees (name, batch_id) VALUES
('Alice Cooper', 1),
('Bob Martin', 1),
('Charlie Rose', 1),
('Diana Prince', 2),
('Edward Norton', 2),
('Fiona Apple', 3),
('George Lucas', 3),
('Helen Hunt', 4),
('Ian McKellen', 4),
('Julia Roberts', 5);

-- Insert Trainer Availability
INSERT INTO trainer_availability (trainer_id, available_date, start_time, end_time, is_booked) VALUES
(1, '2024-01-15', '09:00:00', '17:00:00', true),
(1, '2024-01-16', '09:00:00', '17:00:00', true),
(1, '2024-01-17', '09:00:00', '17:00:00', false),
(2, '2024-01-20', '09:00:00', '17:00:00', true),
(2, '2024-01-21', '09:00:00', '17:00:00', false),
(3, '2024-01-10', '09:00:00', '17:00:00', true),
(4, '2024-02-15', '09:00:00', '17:00:00', true),
(5, '2024-02-20', '09:00:00', '17:00:00', false);

-- Insert Attendance Records
INSERT INTO attendance (trainee_id, batch_id, date, status) VALUES
(1, 1, '2024-01-15', 'PRESENT'),
(2, 1, '2024-01-15', 'PRESENT'),
(3, 1, '2024-01-15', 'ABSENT'),
(1, 1, '2024-01-16', 'PRESENT'),
(2, 1, '2024-01-16', 'LATE'),
(3, 1, '2024-01-16', 'PRESENT'),
(4, 2, '2024-02-01', 'PRESENT'),
(5, 2, '2024-02-01', 'PRESENT'),
(6, 3, '2024-01-20', 'PRESENT'),
(7, 3, '2024-01-20', 'ABSENT');