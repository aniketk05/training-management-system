import api from '../api/axios'

export default {
  getAllTrainees() {
    return api.get('/trainees')
  },

  getTraineeById(id) {
    return api.get(`/trainees/${id}`)
  },

  createTrainee(trainee) {
    return api.post('/trainees', trainee)
  },

  enrollInBatch(enrollment) {
    return api.post('/trainees/enroll', enrollment)
  },

  markAttendance(attendance) {
    return api.post('/trainees/attendance', attendance)
  }
}