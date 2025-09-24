<template>
  <div>
    <div class="mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Dashboard</h1>
      <p class="text-gray-600 mt-2">Overview of training management system</p>
    </div>

    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-500 text-sm">Total Trainers</p>
            <p class="text-2xl font-bold text-gray-800">{{ stats.trainers }}</p>
          </div>
          <UserIcon class="w-12 h-12 text-primary-500" />
        </div>
      </div>

      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-500 text-sm">Total Trainees</p>
            <p class="text-2xl font-bold text-gray-800">{{ stats.trainees }}</p>
          </div>
          <UserGroupIcon class="w-12 h-12 text-green-500" />
        </div>
      </div>

      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-500 text-sm">Active Batches</p>
            <p class="text-2xl font-bold text-gray-800">{{ stats.batches }}</p>
          </div>
          <AcademicCapIcon class="w-12 h-12 text-purple-500" />
        </div>
      </div>

      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-gray-500 text-sm">Avg Attendance</p>
            <p class="text-2xl font-bold text-gray-800">{{ stats.avgAttendance }}%</p>
          </div>
          <ChartBarIcon class="w-12 h-12 text-orange-500" />
        </div>
      </div>
    </div>

    <!-- Charts -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
      <div class="h-80">
        <AvailabilityChart />
      </div>
      <div class="h-80">
        <EnrollmentChart />
      </div>
    </div>

    <div class="h-96">
      <AttendanceChart />
    </div>

    <!-- Recent Activities Table -->
    <div class="card mt-8">
      <h3 class="text-lg font-semibold mb-4">Recent Activities</h3>
      <div class="overflow-x-auto">
        <table class="w-full">
          <thead>
            <tr class="border-b">
              <th class="text-left py-2 px-4">Date</th>
              <th class="text-left py-2 px-4">Type</th>
              <th class="text-left py-2 px-4">Description</th>
              <th class="text-left py-2 px-4">Status</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="activity in recentActivities" :key="activity.id" class="border-b">
              <td class="py-2 px-4">{{ formatDate(activity.date) }}</td>
              <td class="py-2 px-4">{{ activity.type }}</td>
              <td class="py-2 px-4">{{ activity.description }}</td>
              <td class="py-2 px-4">
                <span :class="[
                  'px-2 py-1 rounded-full text-xs',
                  activity.status === 'completed' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'
                ]">
                  {{ activity.status }}
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
import { ref, onMounted } from 'vue'
import { 
  UserIcon, 
  UserGroupIcon, 
  AcademicCapIcon, 
  ChartBarIcon 
} from '@heroicons/vue/24/outline'
import { format } from 'date-fns'
import AvailabilityChart from '../components/charts/AvailabilityChart.vue'
import EnrollmentChart from '../components/charts/EnrollmentChart.vue'
import AttendanceChart from '../components/charts/AttendanceChart.vue'
import trainerService from '../services/trainerService'
import traineeService from '../services/traineeService'
import batchService from '../services/batchService'

const stats = ref({
  trainers: 0,
  trainees: 0,
  batches: 0,
  avgAttendance: 85
})

const recentActivities = ref([
  { id: 1, date: new Date(), type: 'Enrollment', description: 'John Doe enrolled in Java Programming', status: 'completed' },
  { id: 2, date: new Date(), type: 'Attendance', description: 'Batch A marked attendance', status: 'completed' },
  { id: 3, date: new Date(), type: 'Availability', description: 'Trainer Smith updated availability', status: 'pending' }
])

onMounted(async () => {
  try {
    const [trainers, trainees, batches] = await Promise.all([
      trainerService.getAllTrainers(),
      traineeService.getAllTrainees(),
      batchService.getAllBatches()
    ])
    
    stats.value = {
      trainers: trainers.length,
      trainees: trainees.length,
      batches: batches.length,
      avgAttendance: 85
    }
  } catch (error) {
    console.error('Failed to load stats:', error)
  }
})

function formatDate(date) {
  return format(new Date(date), 'MMM dd, yyyy')
}
</script>