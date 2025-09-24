import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import TrainerAvailability from '../views/TrainerAvailability.vue'
import TraineeEnrollment from '../views/TraineeEnrollment.vue'
import AttendanceForm from '../views/AttendanceForm.vue'

const routes = [
  {
    path: '/',
    name: 'Dashboard',
    component: Dashboard,
    meta: { title: 'Dashboard' }
  },
  {
    path: '/trainer-availability',
    name: 'TrainerAvailability',
    component: TrainerAvailability,
    meta: { title: 'Trainer Availability' }
  },
  {
    path: '/trainee-enrollment',
    name: 'TraineeEnrollment',
    component: TraineeEnrollment,
    meta: { title: 'Trainee Enrollment' }
  },
  {
    path: '/attendance',
    name: 'AttendanceForm',
    component: AttendanceForm,
    meta: { title: 'Mark Attendance' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = `${to.meta.title} | Training Management System`
  next()
})

export default router