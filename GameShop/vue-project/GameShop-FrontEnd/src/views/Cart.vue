<template>
  <v-btn @click="router.push({ name: 'Catalog' })">Back to catalog</v-btn>
  <div v-if="!store.cart.length" style="text-align: center">
    <h1>Empty Cart ...</h1>
  </div>
  <div class="cart-items" v-else>
    <div
      class="cart-item"
      v-for="item in store.cart"
      :key="item.gameId"
    >
      <div class="item-details">
        <img :src="item.photoUrl" alt="" />
        <span>Title: {{ item.title }}</span>
        <span>Category: {{ (item.categories || []).map(cat => cat.categoryName).join(', ') }}</span>
        <span>Price: ${{ item.price }}</span>
        <v-btn @click="removeFromCart(item.gameId)">Remove</v-btn>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent } from 'vue';
import { productsStore } from '@/stores/products';
import { useRouter } from 'vue-router';

export default defineComponent({
  name: 'CartView',
  setup() {
    const store = productsStore();
    const router = useRouter();

    const removeFromCart = (id) => {
      store.removeFromCart(id);
    };

    return {
      store,
      router,
      removeFromCart,
    };
  },
});
</script>

<style scoped>
.item-details {
  display: flex;
  flex-direction: column;
  margin-bottom: 32px;
  box-shadow: 0 0 17px 6px #e7e7e7;
  border-radius: 8px;
  padding: 16px;
}

.item-details img {
  width: 20%;
  margin-bottom: 16px;
}
</style>
