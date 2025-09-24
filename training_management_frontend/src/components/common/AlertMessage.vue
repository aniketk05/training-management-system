<template>
  <div 
    v-if="visible"
    :class="[
      'px-4 py-3 rounded-lg mb-4 flex items-center justify-between',
      typeClasses[type]
    ]"
  >
    <div class="flex items-center">
      <component :is="icon" class="w-5 h-5 mr-2" />
      <span>{{ message }}</span>
    </div>
    <button @click="$emit('close')" class="ml-4">
      <XMarkIcon class="w-5 h-5" />
    </button>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { 
  CheckCircleIcon, 
  ExclamationCircleIcon, 
  InformationCircleIcon,
  XMarkIcon 
} from '@heroicons/vue/24/outline'

const props = defineProps({
  type: {
    type: String,
    default: 'info',
    validator: (value) => ['success', 'error', 'warning', 'info'].includes(value)
  },
  message: {
    type: String,
    required: true
  },
  visible: {
    type: Boolean,
    default: true
  }
})

defineEmits(['close'])

const typeClasses = {
  success: 'bg-green-100 text-green-800 border border-green-200',
  error: 'bg-red-100 text-red-800 border border-red-200',
  warning: 'bg-yellow-100 text-yellow-800 border border-yellow-200',
  info: 'bg-blue-100 text-blue-800 border border-blue-200'
}

const icon = computed(() => {
  const icons = {
    success: CheckCircleIcon,
    error: ExclamationCircleIcon,
    warning: ExclamationCircleIcon,
    info: InformationCircleIcon
  }
  return icons[props.type]
})
</script>