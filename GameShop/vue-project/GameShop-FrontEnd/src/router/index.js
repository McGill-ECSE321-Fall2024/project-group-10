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
import ManagePlatforms from '@/views/manager/ManagePlatforms.vue';
import AddEmployee from '@/views/manager/AddEmployee.vue';
import UpdateAccount from '@/views/UpdateAccount.vue';
import ArchiveEmployee from '@/views/manager/ArchiveEmployee.vue';
import ListEmployees from '@/views/manager/ListEmployees.vue';
import ListCustomers from '@/views/manager/ListCustomers.vue';
import EmployeeDashboard from '@/views/employee/EmployeeDashboard.vue';
import ViewEmployees from '@/views/employee/ViewEmployees.vue';
import ViewCustomers from '@/views/employee/ViewCustomers.vue';

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
    // Removed meta: { requiresAuth: true } to allow access without login
  },
  {
    path: '/checkout',
    name: 'Checkout',
    component: Checkout,
    meta: { requiresAuth: true, role: 'CUSTOMER' }, // Require login here
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
    path: '/add-employee',
    name: 'AddEmployee',
    component: AddEmployee,
  },
  {
    path: "/update-account",
    name: "UpdateAccount",
    component: UpdateAccount,
  },
  {
    path: "/logout",
    name: "Logout",
    component: () => import("@/views/Logout.vue"),
    //meta: { requiresAuth: true }, // Optional: Restrict access to authenticated users
  },
  {
    path: '/employee',
    name: 'EmployeeDashboard',
    component: EmployeeDashboard,
    meta: { requiresAuth: false, role: 'EMPLOYEE' },
    children: [{
      path: "/employee/view-employees",
      name: "ViewEmployees",
      component: ViewEmployees,
    },
    {
      path: "/employee/view-customers",
      name: "ViewCustomers",
      component: ViewCustomers,
    },]
  },
  {
    path: '/manager',
    name: 'ManagerDashboard',
    component: ManagerDashboard,
    meta: { requiresAuth: false, role: 'MANAGER' },
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
      {
        path: 'platforms',
        name: 'ManagePlatforms',
        component: ManagePlatforms,
      },
      {
        path: "/manager/archive-employee",
        name: "ArchiveEmployee",
        component: ArchiveEmployee,
      },
      {
        path: "/manager/list-employees",
        name: "ListEmployees",
        component: ListEmployees,
      },
      {
        path: "/manager/list-customers",
        name: "ListCustomers",
        component: ListCustomers,
      },
      // Add other manager routes as needed
    ],
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  const auth = useAuthStore();

  if (to.meta.requiresAuth) {
    if (!auth.user) {
      return next({ name: "Login" });
    }

    if (to.meta.role && auth.accountType !== to.meta.role.toUpperCase()) {
      return next({ name: "Catalog" });
    }
  }

  next();
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
