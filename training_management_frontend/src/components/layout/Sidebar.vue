<template>
  <aside 
    :class="[
      'fixed left-0 top-0 h-full bg-gray-900 text-white transition-all duration-300 z-20',
      isOpen ? 'w-64' : 'w-20'
    ]"
  >
    <div class="p-4">
      <div class="flex items-center justify-between mb-8">
        <h1 :class="['font-bold text-xl transition-all', isOpen ? 'block' : 'hidden']">
          TMS
        </h1>
        <button 
          @click="$emit('toggle')"
          class="p-2 rounded-lg hover:bg-gray-800 transition-colors"
        >
          <Bars3Icon v-if="!isOpen" class="w-6 h-6" />
          <XMarkIcon v-else class="w-6 h-6" />
        </button>
      </div>

      <nav>
        <router-link
          v-for="item in menuItems"
          :key="item.path"
          :to="item.path"
          class="flex items-center p-3 mb-2 rounded-lg hover:bg-gray-800 transition-colors"
          active-class="bg-primary-600"
        >
          <component :is="item.icon" class="w-6 h-6" />
          <span :class="['ml-3 transition-all', isOpen ? 'block' : 'hidden']">
            {{ item.label }}
          </span>
        </router-link>
      </nav>
    </div>
  </aside>
</template>

<script setup>
import { 
  HomeIcon, 
  CalendarIcon, 
  UserGroupIcon, 
  ClipboardDocumentCheckIcon,
  Bars3Icon,
  XMarkIcon 
} from '@heroicons/vue/24/outline'

defineProps({
  isOpen: {
    type: Boolean,
    default: true
  }
})

defineEmits(['toggle'])

const menuItems = [
  { path: '/', label: 'Dashboard', icon: HomeIcon },
  { path: '/trainer-availability', label: 'Trainer Availability', icon: CalendarIcon },
  { path: '/trainee-enrollment', label: 'Trainee Enrollment', icon: UserGroupIcon },
  { path: '/attendance', label: 'Mark Attendance', icon: ClipboardDocumentCheckIcon }
]
</script>