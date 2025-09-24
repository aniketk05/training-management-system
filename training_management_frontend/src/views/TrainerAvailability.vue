<template>
  <div>
    <div class="mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Trainer Availability</h1>
      <p class="text-red-600 mt-2">Set availability for trainers</p>
    </div>

    <AlertMessage 
      v-if="alert.show" 
      :type="alert.type" 
      :message="alert.message"
      @close="alert.show = false"
    />

    <div class="card">
      <form @submit.prevent="submitAvailability">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Select Trainer
            </label>
            <select v-model="form.trainerId" class="input-field" required>
              <option value="">Choose a trainer</option>
              <option v-for="trainer in trainers" :key="trainer.id" :value="trainer.id">
                {{ trainer.name }} - {{ trainer.location }}
              </option>
            </select>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Available Date
            </label>
            <input 
              v-model="form.availableDate" 
              type="date" 
              class="input-field" 
              required
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Start Time
            </label>
            <input 
              v-model="form.startTime" 
              type="time" 
              class="input-field" 
              required
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              End Time
            </label>
            <input 
              v-model="form.endTime" 
              type="time" 
              class="input-field" 
              required
            />
          </div>
        </div>

        <div class="mt-6 flex justify-end space-x-4">
          <button type="button" @click="resetForm" class="btn-secondary">
            Reset
          </button>
          <button type="submit" :disabled="loading" class="btn-primary">
            {{ loading ? 'Saving...' : 'Save Availability' }}
          </button>
        </div>
      </form>
    </div>

    <!-- Availability List -->
    <div class="card mt-6" v-if="selectedTrainer">
      <h3 class="text-lg font-semibold mb-4">
        Current Availability for {{ selectedTrainerName }}
      </h3>
      <div class="overflow-x-auto">
        <table class="w-full">
          <thead>
            <tr class="border-b">
              <th class="text-left py-2 px-4">Date</th>
              <th class="text-left py-2 px-4">Start Time</th>
              <th class="text-left py-2 px-4">End Time</th>
              <th class="text-left py-2 px-4">Status</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="slot in availabilitySlots" :key="slot.id" class="border-b">
              <td class="py-2 px-4">{{ formatDate(slot.availableDate) }}</td>
              <td class="py-2 px-4">{{ slot.startTime }}</td>
              <td class="py-2 px-4">{{ slot.endTime }}</td>
              <td class="py-2 px-4">
                <span :class="[
                  'px-2 py-1 rounded-full text-xs',
                  slot.booked ? 'bg-red-100 text-red-800' : 'bg-green-100 text-green-800'
                ]">
                  {{ slot.booked ? 'Booked' : 'Available' }}
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
import { ref, onMounted, watch, computed } from 'vue'
import { format } from 'date-fns'
import trainerService from '../services/trainerService'
import AlertMessage from '../components/common/AlertMessage.vue'

const trainers = ref([])
const availabilitySlots = ref([])
const loading = ref(false)

const form = ref({
  trainerId: '',
  availableDate: '',
  startTime: '',
  endTime: ''
})

const alert = ref({
  show: false,
  type: 'success',
  message: ''
})

const selectedTrainer = computed(() => form.value.trainerId)
const selectedTrainerName = computed(() => {
  const trainer = trainers.value.find(t => t.id === parseInt(form.value.trainerId))
  return trainer ? trainer.name : ''
})

onMounted(async () => {
  try {
    trainers.value = await trainerService.getAllTrainers()
  } catch (error) {
    console.error('Failed to load trainers:', error)
    showAlert('error', 'Failed to load trainers')
  }
})

watch(selectedTrainer, async (newValue) => {
  if (newValue) {
    try {
      availabilitySlots.value = await trainerService.getAvailability(newValue)
    } catch (error) {
      console.error('Failed to load availability:', error)
    }
  } else {
    availabilitySlots.value = []
  }
})

async function submitAvailability() {
  loading.value = true
  try {
    await trainerService.setAvailability(form.value)
    showAlert('success', 'Availability saved successfully!')
    
    // Reload availability list
    if (form.value.trainerId) {
      availabilitySlots.value = await trainerService.getAvailability(form.value.trainerId)
    }
    
    // Reset form but keep trainer selected
    const trainerId = form.value.trainerId
    resetForm()
    form.value.trainerId = trainerId
  } catch (error) {
    console.error('Failed to save availability:', error)
    showAlert('error', 'Failed to save availability')
  } finally {
    loading.value = false
  }
}

function resetForm() {
  form.value = {
    trainerId: '',
    availableDate: '',
    startTime: '',
    endTime: ''
  }
}

function showAlert(type, message) {
  alert.value = { show: true, type, message }
  setTimeout(() => {
    alert.value.show = false
  }, 5000)
}

function formatDate(date) {
  return format(new Date(date), 'MMM dd, yyyy')
}
</script>