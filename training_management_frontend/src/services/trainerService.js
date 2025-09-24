import api from '../api/axios'

export default {
  getAllTrainers() {
    return api.get('/trainers')
  },

  getTrainerById(id) {
    return api.get(`/trainers/${id}`)
  },

  createTrainer(trainer) {
    return api.post('/trainers', trainer)
  },

  setAvailability(availability) {
    return api.post(`/trainers/${availability.trainerId}/availability`, availability)
  },

  getAvailability(trainerId) {
    return api.get(`/trainers/${trainerId}/availability`)
  },

  getBatchProgress(trainerId, batchId) {
    return api.get(`/trainers/${trainerId}/batches/${batchId}/progress`)
  },

  getAvailableTrainersByLocation(location) {
    return api.get('/trainers/available', { params: { location } })
  }
}