<template>
  <header class="bg-white shadow-sm border-b border-gray-200">
    <div class="px-6 py-4">
      <div class="flex items-center justify-between">
        <div class="flex items-center">
          <button 
            @click="$emit('toggleSidebar')"
            class="p-2 rounded-lg hover:bg-gray-100 lg:hidden"
          >
            <Bars3Icon class="w-6 h-6" />
          </button>
          <h2 class="text-2xl font-semibold text-gray-800 ml-4">
            Training Management System
          </h2>
        </div>
        
        <div class="flex items-center space-x-4">
          <span class="text-sm text-gray-600">
            {{ currentDateTime }}
          </span>
          <div class="w-10 h-10 bg-primary-500 rounded-full flex items-center justify-center text-white font-semibold">
            A
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { Bars3Icon } from '@heroicons/vue/24/outline'
import { format } from 'date-fns'

defineEmits(['toggleSidebar'])

const currentDateTime = ref('')

let interval

onMounted(() => {
  updateDateTime()
  interval = setInterval(updateDateTime, 1000)
})

onUnmounted(() => {
  clearInterval(interval)
})

function updateDateTime() {
  currentDateTime.value = format(new Date(), 'MMM dd, yyyy HH:mm:ss')
}
</script>