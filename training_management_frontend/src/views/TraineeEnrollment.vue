<template>
  <div>
    <div class="mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Trainee Enrollment</h1>
      <p class="text-gray-600 mt-2">Enroll trainees into batches</p>
    </div>

    <AlertMessage 
      v-if="alert.show" 
      :type="alert.type" 
      :message="alert.message"
      @close="alert.show = false"
    />

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Enrollment Form -->
      <div class="card">
        <h3 class="text-lg font-semibold mb-4">New Enrollment</h3>
        <form @submit.prevent="enrollTrainee">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                Select Trainee
              </label>
              <select v-model="enrollment.traineeId" class="input-field" required>
                <option value="">Choose a trainee</option>
                <option v-for="trainee in availableTrainees" :key="trainee.id" :value="trainee.id">
                  {{ trainee.name }}
                </option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                Select Batch
              </label>
              <select v-model="enrollment.batchId" class="input-field" required>
                <option value="">Choose a batch</option>
                <option v-for="batch in batches" :key="batch.id" :value="batch.id">
                  {{ batch.course }} - {{ batch.location }}
                </option>
              </select>
            </div>

            <div v-if="selectedBatch" class="bg-gray-50 p-4 rounded-lg">
              <h4 class="font-medium mb-2">Batch Details</h4>
              <p class="text-sm text-gray-600">Course: {{ selectedBatch.course }}</p>
              <p class="text-sm text-gray-600">Location: {{ selectedBatch.location }}</p>
              <p class="text-sm text-gray-600">Trainer: {{ selectedBatch.trainer?.name }}</p>
              <p class="text-sm text-gray-600">
                Duration: {{ formatDate(selectedBatch.startDate) }} - {{ formatDate(selectedBatch.endDate) }}
              </p>
            </div>
          </div>

          <div class="mt-6">
            <button type="submit" :disabled="loading" class="btn-primary w-full">
              {{ loading ? 'Enrolling...' : 'Enroll Trainee' }}
            </button>
          </div>
        </form>
      </div>

      <!-- Create New Trainee -->
      <div class="card">
        <h3 class="text-lg font-semibold mb-4">Create New Trainee</h3>
        <form @submit.prevent="createTrainee">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                Trainee Name
              </label>
              <input 
                v-model="newTrainee.name" 
                type="text" 
                class="input-field" 
                placeholder="Enter trainee name"
                required
              />
            </div>

            <button type="submit" :disabled="creatingTrainee" class="btn-secondary w-full">
              {{ creatingTrainee ? 'Creating...' : 'Create Trainee' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Enrolled Trainees Table -->
    <div class="card mt-6">
      <h3 class="text-lg font-semibold mb-4">Recently Enrolled Trainees</h3>
      <div class="overflow-x-auto">
        <table class="w-full">
          <thead>
            <tr class="border-b">
              <th class="text-left py-2 px-4">Trainee Name</th>
              <th class="text-left py-2 px-4">Batch</th>
              <th class="text-left py-2 px-4">Course</th>
              <th class="text-left py-2 px-4">Location</th>
              <th class="text-left py-2 px-4">Status</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="trainee in enrolledTrainees" :key="trainee.id" class="border-b">
              <td class="py-2 px-4">{{ trainee.name }}</td>
              <td class="py-2 px-4">{{ trainee.batch?.id }}</td>
              <td class="py-2 px-4">{{ trainee.batch?.course }}</td>
              <td class="py-2 px-4">{{ trainee.batch?.location }}</td>
              <td class="py-2 px-4">
                <span class="px-2 py-1 rounded-full text-xs bg-green-100 text-green-800">
                  Enrolled
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { format } from 'date-fns'
import traineeService from '../services/traineeService'
import batchService from '../services/batchService'
import AlertMessage from '../components/common/AlertMessage.vue'

const trainees = ref([])
const batches = ref([])
const enrolledTrainees = ref([])
const loading = ref(false)
const creatingTrainee = ref(false)

const enrollment = ref({
  traineeId: '',
  batchId: ''
})

const newTrainee = ref({
  name: ''
})

const alert = ref({
  show: false,
  type: 'success',
  message: ''
})

const availableTrainees = computed(() => {
  return trainees.value.filter(t => !t.batch)
})

const selectedBatch = computed(() => {
  return batches.value.find(b => b.id === parseInt(enrollment.value.batchId))
})

onMounted(async () => {
  await loadData()
})

async function loadData() {
  try {
    const [traineeData, batchData] = await Promise.all([
      traineeService.getAllTrainees(),
      batchService.getAllBatches()
    ])
    
    trainees.value = traineeData
    batches.value = batchData
    enrolledTrainees.value = traineeData.filter(t => t.batch)
  } catch (error) {
    console.error('Failed to load data:', error)
    showAlert('error', 'Failed to load data')
  }
}

async function enrollTrainee() {
  loading.value = true
  try {
    await traineeService.enrollInBatch(enrollment.value)
    showAlert('success', 'Trainee enrolled successfully!')
    
    enrollment.value = {
      traineeId: '',
      batchId: ''
    }
    
    await loadData()
  } catch (error) {
    console.error('Failed to enroll trainee:', error)
    showAlert('error', 'Failed to enroll trainee')
  } finally {
    loading.value = false
  }
}

async function createTrainee() {
  creatingTrainee.value = true
  try {
    await traineeService.createTrainee(newTrainee.value)
    showAlert('success', 'Trainee created successfully!')
    
    newTrainee.value.name = ''
    await loadData()
  } catch (error) {
    console.error('Failed to create trainee:', error)
    showAlert('error', 'Failed to create trainee')
  } finally {
    creatingTrainee.value = false
  }
}

function showAlert(type, message) {
  alert.value = { show: true, type, message }
  setTimeout(() => {
    alert.value.show = false
  }, 5000)
}

function formatDate(date) {
  if (!date) return 'N/A'
  return format(new Date(date), 'MMM dd, yyyy')
}
</script>