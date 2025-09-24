<template>
  <div>
    <div class="mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Mark Attendance</h1>
      <p class="text-gray-600 mt-2">Record trainee attendance for batches</p>
    </div>

    <AlertMessage 
      v-if="alert.show" 
      :type="alert.type" 
      :message="alert.message"
      @close="alert.show = false"
    />

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Attendance Form -->
      <div class="lg:col-span-2">
        <div class="card">
          <h3 class="text-lg font-semibold mb-4">Mark Attendance</h3>
          
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                Select Batch
              </label>
              <select v-model="selectedBatchId" @change="loadBatchTrainees" class="input-field">
                <option value="">Choose a batch</option>
                <option v-for="batch in batches" :key="batch.id" :value="batch.id">
                  {{ batch.course }} - {{ batch.location }}
                </option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                Date
              </label>
              <input 
                v-model="attendanceDate" 
                type="date" 
                class="input-field"
                :max="today"
              />
            </div>
          </div>

          <div v-if="batchTrainees.length > 0" class="overflow-x-auto">
            <table class="w-full">
              <thead>
                <tr class="border-b">
                  <th class="text-left py-2 px-4">Trainee Name</th>
                  <th class="text-center py-2 px-4">Present</th>
                  <th class="text-center py-2 px-4">Absent</th>
                  <th class="text-center py-2 px-4">Late</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="trainee in batchTrainees" :key="trainee.id" class="border-b">
                  <td class="py-3 px-4">{{ trainee.name }}</td>
                  <td class="text-center">
                    <input 
                      type="radio" 
                      :name="`attendance-${trainee.id}`"
                      :value="'PRESENT'"
                      v-model="attendanceRecords[trainee.id]"
                      class="w-4 h-4 text-primary-600"
                    />
                  </td>
                  <td class="text-center">
                    <input 
                      type="radio" 
                      :name="`attendance-${trainee.id}`"
                      :value="'ABSENT'"
                      v-model="attendanceRecords[trainee.id]"
                      class="w-4 h-4 text-primary-600"
                    />
                  </td>
                  <td class="text-center">
                    <input 
                      type="radio" 
                      :name="`attendance-${trainee.id}`"
                      :value="'LATE'"
                      v-model="attendanceRecords[trainee.id]"
                      class="w-4 h-4 text-primary-600"
                    />
                  </td>
                </tr>
              </tbody>
            </table>

            <div class="mt-6 flex justify-end space-x-4">
              <button @click="markAllPresent" class="btn-secondary">
                Mark All Present
              </button>
              <button @click="submitAttendance" :disabled="loading" class="btn-primary">
                {{ loading ? 'Saving...' : 'Save Attendance' }}
              </button>
            </div>
          </div>

          <div v-else-if="selectedBatchId" class="text-center py-8 text-gray-500">
            No trainees found in this batch
          </div>
        </div>
      </div>

      <!-- Attendance Summary -->
      <div>
        <div class="card">
          <h3 class="text-lg font-semibold mb-4">Today's Summary</h3>
          
          <div class="space-y-4">
            <div class="flex justify-between items-center p-3 bg-green-50 rounded-lg">
              <span class="text-green-700">Present</span>
              <span class="font-bold text-green-700">{{ attendanceSummary.present }}</span>
            </div>

            <div class="flex justify-between items-center p-3 bg-red-50 rounded-lg">
              <span class="text-red-700">Absent</span>
              <span class="font-bold text-red-700">{{ attendanceSummary.absent }}</span>
            </div>

            <div class="flex justify-between items-center p-3 bg-yellow-50 rounded-lg">
              <span class="text-yellow-700">Late</span>
              <span class="font-bold text-yellow-700">{{ attendanceSummary.late }}</span>
            </div>

            <div class="pt-4 border-t">
              <div class="flex justify-between items-center">
                <span class="text-gray-600">Total</span>
                <span class="font-bold text-gray-800">{{ attendanceSummary.total }}</span>
              </div>
            </div>

            <div v-if="attendanceSummary.total > 0" class="pt-4">
              <div class="text-center">
                <div class="text-3xl font-bold text-primary-600">
                  {{ attendanceRate }}%
                </div>
                <div class="text-sm text-gray-600 mt-1">Attendance Rate</div>
              </div>
            </div>
          </div>
        </div>
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

const batches = ref([])
const batchTrainees = ref([])
const selectedBatchId = ref('')
const attendanceDate = ref(format(new Date(), 'yyyy-MM-dd'))
const attendanceRecords = ref({})
const loading = ref(false)

const alert = ref({
  show: false,
  type: 'success',
  message: ''
})

const today = format(new Date(), 'yyyy-MM-dd')

const attendanceSummary = computed(() => {
  const records = Object.values(attendanceRecords.value)
  return {
    present: records.filter(r => r === 'PRESENT').length,
    absent: records.filter(r => r === 'ABSENT').length,
    late: records.filter(r => r === 'LATE').length,
    total: batchTrainees.value.length
  }
})

const attendanceRate = computed(() => {
  const { present, late, total } = attendanceSummary.value
  if (total === 0) return 0
  return Math.round(((present + late) / total) * 100)
})

onMounted(async () => {
  try {
    batches.value = await batchService.getAllBatches()
  } catch (error) {
    console.error('Failed to load batches:', error)
    showAlert('error', 'Failed to load batches')
  }
})

async function loadBatchTrainees() {
  if (!selectedBatchId.value) {
    batchTrainees.value = []
    attendanceRecords.value = {}
    return
  }

  try {
    const trainees = await traineeService.getAllTrainees()
    batchTrainees.value = trainees.filter(t => t.batch?.id === parseInt(selectedBatchId.value))
    
    // Initialize attendance records
    attendanceRecords.value = {}
    batchTrainees.value.forEach(trainee => {
      attendanceRecords.value[trainee.id] = 'PRESENT'
    })
  } catch (error) {
    console.error('Failed to load trainees:', error)
    showAlert('error', 'Failed to load trainees')
  }
}

function markAllPresent() {
  batchTrainees.value.forEach(trainee => {
    attendanceRecords.value[trainee.id] = 'PRESENT'
  })
}

async function submitAttendance() {
  if (!selectedBatchId.value || !attendanceDate.value) {
    showAlert('warning', 'Please select a batch and date')
    return
  }

  loading.value = true
  try {
    const promises = Object.entries(attendanceRecords.value).map(([traineeId, status]) => {
      return traineeService.markAttendance({
        traineeId: parseInt(traineeId),
        batchId: parseInt(selectedBatchId.value),
        date: attendanceDate.value,
        status
      })
    })

    await Promise.all(promises)
    showAlert('success', 'Attendance saved successfully!')
  } catch (error) {
    console.error('Failed to save attendance:', error)
    showAlert('error', 'Failed to save attendance')
  } finally {
    loading.value = false
  }
}

function showAlert(type, message) {
  alert.value = { show: true, type, message }
  setTimeout(() => {
    alert.value.show = false
  }, 5000)
}
</script>