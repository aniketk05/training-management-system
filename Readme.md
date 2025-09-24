Training Management System - Quick Start Guide
🚀 Setup and Run (Docker)
Prerequisites
Docker (20.10+)
Docker Compose (1.29+)
Installation Steps
Bash

# 1. Clone the repository
git clone https://github.com/aniketk05/training-management-system.git
cd training-management-system

# 2. Create environment file
cp .env.example .env

# 3. Build and start all services
docker-compose up -d --build

# 4. Verify services are running
docker-compose ps
Access Points
Frontend: http://localhost
Backend API: http://localhost:8080/api
Database: localhost:3306
🔑 Default Credentials
Database
text

Username: training_user
Password: Training@2024
Database: training_db
Sample Users (Pre-loaded)
8 Trainers (John Smith, Sarah Johnson, etc.)
15 Trainees (Alice Cooper, Bob Martin, etc.)
8 Active Batches
📝 Example Workflows
1️⃣ Add Trainer Availability
Go to http://localhost → Click "Trainer Availability"
Select Trainer: "Dr. John Smith - New York"
Set Date: 2024-02-01
Set Time: 09:00 to 17:00
Click "Save Availability"
Via API:

Bash

curl -X POST http://localhost:8080/api/trainers/1/availability \
  -H "Content-Type: application/json" \
  -d '{"trainerId": 1, "availableDate": "2024-02-01", "startTime": "09:00:00", "endTime": "17:00:00"}'
2️⃣ Enroll a Trainee
Go to "Trainee Enrollment"
Create new trainee (if needed):
Name: "John Doe"
Click "Create Trainee"
Select Trainee: "John Doe"
Select Batch: "Java Programming - New York"
Click "Enroll Trainee"
Via API:

Bash

curl -X POST http://localhost:8080/api/trainees/enroll \
  -H "Content-Type: application/json" \
  -d '{"traineeId": 1, "batchId": 1}'
3️⃣ Mark Attendance
Go to "Mark Attendance"
Select Batch: "Java Programming - New York"
Select Date: Today
Mark each trainee: Present / Absent / Late
Click "Save Attendance"
Quick Option: Click "Mark All Present" for bulk marking

Via API:

Bash

curl -X POST http://localhost:8080/api/trainees/attendance \
  -H "Content-Type: application/json" \
  -d '{"traineeId": 1, "batchId": 1, "date": "2024-02-01", "status": "PRESENT"}'
4️⃣ View Reports
Go to "Dashboard"
View available reports:
Top Cards: Total Trainers, Trainees, Batches, Avg Attendance
Bar Chart: Trainer Availability vs Occupancy
Pie Chart: Batch Enrollment Distribution
Line Chart: Attendance Trends (select batch to view)
Via API:

Bash

# Trainer availability report
curl http://localhost:8080/api/reports/trainer-availability

# Batch enrollments
curl http://localhost:8080/api/reports/batch-enrollments

# Attendance trends
curl "http://localhost:8080/api/reports/attendance-trends?batchId=1&startDate=2024-01-01&endDate=2024-01-31"
🛠️ Common Commands
Bash

# Start services
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f

# Restart services
docker-compose restart

# Clean everything
docker-compose down -v
❓ Troubleshooting
Bash

# Port conflict (kill process using port)
sudo lsof -i :8080
sudo kill -9 <PID>

# Database connection issue
docker-compose restart db

# Reset everything
docker-compose down -v
docker-compose up -d --build
Ready to use! Open http://localhost to start using the system.