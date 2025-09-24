<template>
  <div class="card h-full">
    <h3 class="text-lg font-semibold mb-4">Attendance Trends</h3>
    <div class="mb-4" >
      <select v-model="selectedBatch" @change="loadAttendanceData" class="input-field w-48">
        <option value="">Select Batch</option>
        <option v-for="batch in batches" :key="batch.id" :value="batch.id">
          {{ batch.course }}
        </option>
      </select>
    </div>
    <div >
    <Line v-if="chartData"  class="h-56" :data="chartData" :options="chartOptions" />
    <LoadingSpinner v-else-if="loading" />
    <p v-else class="text-gray-500 text-center py-8">Select a batch to view trends</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Line } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  LinearScale,
  CategoryScale,
  PointElement
} from 'chart.js'
import reportService from '../../services/reportService'
import batchService from '../../services/batchService'
import LoadingSpinner from '../common/LoadingSpinner.vue'
import { format, subDays } from 'date-fns'

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  LineElement,
  LinearScale,
  CategoryScale,
  PointElement
)

const chartData = ref(null)
const batches = ref([])
const selectedBatch = ref('')
const loading = ref(false)

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'top',
    }
  },
  scales: {
    y: {
      beginAtZero: true,
      max: 100
    }
  }
}

onMounted(async () => {
  try {
    batches.value = await batchService.getAllBatches()
  } catch (error) {
    console.error('Failed to load batches:', error)
  }
})

async function loadAttendanceData() {
  if (!selectedBatch.value) {
    chartData.value = null
    return
  }

  loading.value = true
  try {
    const endDate = new Date()
    const startDate = subDays(endDate, 30)
    
    const data = await reportService.getAttendanceTrends(
      selectedBatch.value,
      format(startDate, 'yyyy-MM-dd'),
      format(endDate, 'yyyy-MM-dd')
    )
    
    chartData.value = {
      labels: data.map(d => format(new Date(d.date), 'MMM dd')),
      datasets: [{
        label: 'Attendance Rate (%)',
        data: data.map(d => d.attendanceRate),
        borderColor: 'rgb(59, 130, 246)',
        backgroundColor: 'rgba(59, 130, 246, 0.1)',
        tension: 0.1
      }]
    }
  } catch (error) {
    console.error('Failed to load attendance trends:', error)
  } finally {
    loading.value = false
  }
}
</script>