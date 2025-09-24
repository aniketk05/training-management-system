<template>
  <div class="card h-full">
    <h3 class="text-lg font-semibold mb-4">Trainer Availability vs Occupancy</h3>
    <div>
    <Bar class="h-52" v-if="chartData" :data="chartData" :options="chartOptions" />
    <LoadingSpinner v-else />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Bar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
} from 'chart.js'
import reportService from '../../services/reportService'
import LoadingSpinner from '../common/LoadingSpinner.vue'

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

const chartData = ref(null)

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'top',
    },
    title: {
      display: false
    }
  },
  scales: {
    y: {
      beginAtZero: true
    }
  }
}

onMounted(async () => {
  try {
    const data = await reportService.getTrainerAvailabilityReport()
    
    chartData.value = {
      labels: data.map(d => d.trainerName),
      datasets: [
        {
          label: 'Available Days',
          data: data.map(d => d.totalAvailableDays),
          backgroundColor: 'rgba(59, 130, 246, 0.5)',
          borderColor: 'rgb(59, 130, 246)',
          borderWidth: 1
        },
        {
          label: 'Assigned Days',
          data: data.map(d => d.assignedDays),
          backgroundColor: 'rgba(16, 185, 129, 0.5)',
          borderColor: 'rgb(16, 185, 129)',
          borderWidth: 1
        }
      ]
    }
  } catch (error) {
    console.error('Failed to load availability data:', error)
  }
})
</script>