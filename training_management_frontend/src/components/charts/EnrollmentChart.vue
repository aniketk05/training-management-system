<template>
  <div class="card h-full">
    <h3 class="text-lg font-semibold mb-4">Batch Enrollments</h3>
    <div>
    
    <Pie v-if="chartData" class="h-56" :data="chartData" :options="chartOptions" />
    <LoadingSpinner v-else />
    
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Pie } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  CategoryScale
} from 'chart.js'
import reportService from '../../services/reportService'
import LoadingSpinner from '../common/LoadingSpinner.vue'

ChartJS.register(Title, Tooltip, Legend, ArcElement, CategoryScale)

const chartData = ref(null)

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'right',
    }
  }
}

onMounted(async () => {
  try {
    const data = await reportService.getBatchEnrollmentReport()
    console.log(data)
    const colors = [
      'rgba(255, 99, 132, 0.5)',
      'rgba(54, 162, 235, 0.5)',
      'rgba(255, 206, 86, 0.5)',
      'rgba(75, 192, 192, 0.5)',
      'rgba(153, 102, 255, 0.5)',
    ]
    
    chartData.value = {
      labels: data.map(d => d.course),
      datasets: [{
        label: 'Enrolled Students',
        data: data.map(d => d.enrolledCount),
        backgroundColor: colors,
        borderColor: colors.map(c => c.replace('0.5', '1')),
        borderWidth: 1
      }]
    }
  } catch (error) {
    console.error('Failed to load enrollment data:', error)
  }
})
</script>