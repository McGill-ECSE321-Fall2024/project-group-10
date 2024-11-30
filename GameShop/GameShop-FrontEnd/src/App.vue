<template>
  <div>
    <v-app>
      <v-toolbar>
        <router-link
          to="/"
          class="title-link"
          style="
            text-decoration: none;
            color: black;
            margin-left: 15px;
            font-size: 20px;
          "
        >
          <span class="title">Game Shop</span>
        </router-link>
        <v-spacer></v-spacer>
        <v-btn
          v-if="isCustomer"
          @click="goToCart"
          color="primary"
          variant="elevated"
        >
          Cart ({{ cartItemCount }})
        </v-btn>
        <v-btn
          v-if="isManager || isEmployee || isCustomer"
          @click="goToUpdateAccount"
          color="primary"
          variant="elevated"
        >
          Update Account
        </v-btn>
        <v-btn
          v-if="isManager"
          @click="goToManagerDashboard"
          color="primary"
          variant="elevated"
        >
          Manager Dashboard
        </v-btn>
        <v-btn
          v-if="isEmployee"
          @click="goToEmployeeDashboard"
          color="primary"
          variant="elevated"
        >
          Employee Dashboard
        </v-btn>

        <v-btn
          v-if="auth.user"
          @click="router.push({ name: 'Logout' })"
          color="secondary"
          variant="elevated"
        >
          Logout
        </v-btn>
        <v-btn
          v-else
          @click="router.push({ name: 'Login' })"
          color="secondary"
          variant="elevated"
        >
          Login
        </v-btn>
      </v-toolbar>
      <router-view />
    </v-app>
  </div>
</template>

<script>
import { defineComponent, computed } from "vue";
import { useRouter } from "vue-router";
import { useCartStore } from "@/stores/cart";
import { productsStore } from "@/stores/products";
import { useAuthStore } from "@/stores/auth";

export default defineComponent({
  name: "App",
  computed: {
    isManager() {
      const authStore = useAuthStore();
      return authStore.accountType === "MANAGER"; // Adjust according to your role naming
    },
    isEmployee() {
      const authStore = useAuthStore();
      return authStore.accountType === "EMPLOYEE"; // Adjust according to your role naming
    },
    isCustomer() {
      const authStore = useAuthStore();
      return authStore.accountType === "CUSTOMER";
    },
  },

  setup() {
    const router = useRouter();
    const store = productsStore();
    const auth = useAuthStore();
    const cartStore = useCartStore();
    const cartItemCount = computed(() =>
      cartStore.cartItems.reduce((total, item) => total + item.quantity, 0)
    );

    const goToEmployeeDashboard = () => {
      router.push({ name: "EmployeeDashboard" });
    };
    const goToManagerDashboard = () => {
      router.push({ name: "ManagerDashboard" });
    };

    const goToCart = () => {
      router.push({ name: "CartView" });
    };

    const goToUpdateAccount = () => {
      router.push("/update-account");
    };

    const logout = () => {
      auth.logout();
      router.push({ name: "Catalog" });
    };

    return {
      router,
      store,
      auth,
      cartItemCount,
      goToCart,
      logout,
      goToUpdateAccount,
      goToManagerDashboard,
      goToEmployeeDashboard,
    };
  },
});
</script>

<style>
/* Add global styles if needed */
</style>
