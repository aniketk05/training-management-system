import api from '../api/axios'

export default {
  getAllBatches() {
    return api.get('/batches')
  },

  getBatchById(id) {
    return api.get(`/batches/${id}`)
  },

  createBatch(batch) {
    return api.post('/batches', batch)
  },

  getBatchesByLocation(location) {
    return api.get(`/batches/location/${location}`)
  }
}