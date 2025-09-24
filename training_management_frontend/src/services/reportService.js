import api from '../api/axios'

export default {
  getTrainerAvailabilityReport() {
    return api.get('/reports/trainer-availability')
  },

  getBatchEnrollmentReport() {
    return api.get('/reports/batch-enrollments')
  },

  getAttendanceTrends(batchId, startDate, endDate) {
    return api.get('/reports/attendance-trends', {
      params: { batchId, startDate, endDate }
    })
  }
}