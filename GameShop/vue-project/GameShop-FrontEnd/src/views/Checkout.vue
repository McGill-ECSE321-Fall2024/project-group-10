<!-- File: src/views/Checkout.vue -->

<template>
    <v-container>
      <h1>Checkout</h1>
      <!-- Order Summary -->
      <div v-for="item in store.cart" :key="item.game_id" class="checkout-item">
        <p>{{ item.title }} - ${{ item.price }}</p>
      </div>
      <h2>Total: ${{ total }}</h2>
      <!-- Payment and Shipping Details -->
      <!-- For simplicity, we'll just simulate the purchase -->
      <v-btn color="success" @click="completePurchase">Complete Purchase</v-btn>
    </v-container>
  </template>
  
  <script>
  import { defineComponent, computed, onMounted } from 'vue';
  import { productsStore } from '@/stores/products';
  import { useAuthStore } from '@/stores/auth';
  import { useRouter } from 'vue-router';
  
  export default defineComponent({
    name: 'Checkout',
    setup() {
      const store = productsStore();
      const auth = useAuthStore();
      const router = useRouter();
  
      // Ensure the user is authenticated
      onMounted(() => {
        if (!auth.user) {
          router.push({ name: 'Login' });
        }
      });
  
      const total = computed(() => {
        return store.cart.reduce((sum, item) => sum + item.price, 0);
      });
  
      const completePurchase = () => {
        // Implement your purchase logic here
        alert('Purchase completed!');
        // Clear the cart
        store.clearCart();
        // Redirect to a confirmation page or back to the catalog
        router.push({ name: 'Catalog' });
      };
  
      return {
        store,
        total,
        completePurchase,
      };
    },
  });
  </script>
  
  <style scoped>
  .checkout-item {
    margin-bottom: 16px;
  }
  </style>
  