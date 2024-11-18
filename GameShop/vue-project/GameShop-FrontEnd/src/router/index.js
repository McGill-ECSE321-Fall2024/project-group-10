// File: src/router/index.js

import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

// Import views
import Catalog from '@/views/Catalog.vue';
import ProductDetail from '@/views/ProductDetail.vue';
import Cart from '@/views/Cart.vue';
import Login from '@/views/Login.vue';
import Register from '@/views/Register.vue';
import ManagerDashboard from '@/views/manager/ManagerDashboard.vue';
import AddGame from '@/views/manager/AddGame.vue';
import EditGame from '@/views/manager/EditGame.vue';
import Checkout from '@/views/Checkout.vue';
import ManageCategories from '@/views/manager/ManageCategories.vue';

const routes = [
  // ... other routes
  {
    path: '/manager',
    name: 'ManagerDashboard',
    component: ManagerDashboard,
    meta: { requiresAuth: false, role: 'manager' },
    children: [
      {
        path: 'add-game', // Relative path
        name: 'AddGame',
        component: AddGame,
      },
      {
        path: 'edit-game/:id', // Relative path
        name: 'EditGame',
        component: EditGame,
      },
      {
        path: 'categories', // Changed from '/manager/categories' to 'categories'
        name: 'ManageCategories',
        component: ManageCategories,
      },
      // Add other manager routes as needed
    ],
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

// Navigation Guards (no changes)
router.beforeEach((to, from, next) => {
  const auth = useAuthStore();
  if (to.meta.requiresAuth) {
    if (auth.user) {
      if (to.meta.role && auth.user.role !== to.meta.role) {
        next({ name: 'Catalog' });
      } else {
        next();
      }
    } else {
      next({ name: 'Login' });
    }
  } else {
    next();
  }
});

export default router;
