<!-- File: src/views/Cart.vue -->

<template>
  <v-btn @click="router.push({ name: 'Catalog' })">Back to catalog</v-btn>
  <div v-if="!store.cart.length" style="text-align: center">
    <h1>Empty Cart ...</h1>
  </div>
  <div class="cart-items" v-else>
    <div
      class="cart-item"
      v-for="item in store.cart"
      :key="item.game_id"
    >
      <div class="item-details">
        <img :src="item.photoUrl" alt="" />
        <span>Title: {{ item.title }}</span>
        <span>Price: ${{ item.price }}</span>
        <v-btn @click="removeFromCart(item.game_id)">Remove</v-btn>
      </div>
    </div>
    <!-- Proceed to Checkout Button -->
    <v-btn color="success" @click="proceedToCheckout">Proceed to Checkout</v-btn>
  </div>
</template>

<script>
import { defineComponent } from 'vue';
import { productsStore } from '@/stores/products';
import { useRouter } from 'vue-router';
import { useAuthStore } from "@/stores/auth";

export default defineComponent({
  name: 'CartView',
  setup() {
    const store = productsStore();
    const router = useRouter();
    const auth = useAuthStore();

    const removeFromCart = (id) => {
      store.removeFromCart(id);
    };

    const proceedToCheckout = () => {
      if (!auth.user) {
        router.push({ name: 'Login' }); // Require login before checkout
      } else {
        router.push({ name: 'Checkout' }); // Proceed to checkout page
      }
    };

    return {
      store,
      router,
      removeFromCart,
      proceedToCheckout,
    };
  },
});
</script>

<style scoped>
.cart-items {
  padding: 16px;
}

.cart-item {
  display: flex;
  flex-direction: column;
  margin-bottom: 32px;
  box-shadow: 0 0 17px 6px #e7e7e7;
  border-radius: 8px;
  padding: 16px;
}

.item-details {
  display: flex;
  align-items: center;
}

.item-details img {
  width: 100px;
  margin-right: 16px;
  border-radius: 8px;
}
</style>
