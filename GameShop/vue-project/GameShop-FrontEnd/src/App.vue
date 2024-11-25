<template>
  <div>
    <v-app>
      <v-toolbar title="Game Shop">
        <v-spacer></v-spacer>
        <v-btn
          @click="goToCart"
          color="primary"
          variant="elevated"
        >
          Cart ({{ cartItemCount }})
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
import { defineComponent, computed } from 'vue';
import { useRouter } from 'vue-router';
import { productsStore } from '@/stores/products';
import { useAuthStore } from '@/stores/auth';

export default defineComponent({
  name: 'App',
  setup() {
    const router = useRouter();
    const store = productsStore();
    const auth = useAuthStore();

    const cartItemCount = computed(() => store.cart.length);

    const goToCart = () => {
      router.push({ name: 'CartView' });
    };

    const logout = () => {
      auth.logout();
      router.push({ name: "Catalog" });
      //router.push({ name: 'Catalog' });
    };

    return {
      router,
      store,
      auth,
      cartItemCount,
      goToCart,
      logout,
    };
  },
});
</script>

<style>
/* Add global styles if needed */
</style>
