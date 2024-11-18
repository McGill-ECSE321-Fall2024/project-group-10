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

const routes = [
  {
    path: '/',
    name: 'Catalog',
    component: Catalog,
  },
  {
    path: '/product/:id',
    name: 'ProductView',
    component: ProductDetail,
  },
  {
    path: '/cart',
    name: 'CartView',
    component: Cart,
    meta: { requiresAuth: true, role: 'customer' },
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
  },
  {
    path: '/manager',
    name: 'ManagerDashboard',
    component: ManagerDashboard,
    meta: { requiresAuth: true, role: 'manager' },
    children: [
      {
        path: 'add-game',
        name: 'AddGame',
        component: AddGame,
      },
      {
        path: 'edit-game/:id',
        name: 'EditGame',
        component: EditGame,
      },
      // Add other manager routes as needed
    ],
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

// Navigation Guards for Authentication and Authorization
router.beforeEach((to, from, next) => {
  const auth = useAuthStore();
  if (to.meta.requiresAuth) {
    if (auth.user) {
      if (to.meta.role && auth.user.role !== to.meta.role) {
        next({ name: 'Catalog' }); // Redirect unauthorized users
      } else {
        next();
      }
    } else {
      next({ name: 'Login' }); // Redirect to login if not authenticated
    }
  } else {
    next();
  }
});

export default router;
