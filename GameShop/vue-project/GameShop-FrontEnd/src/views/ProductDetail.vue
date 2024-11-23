<template>
  <v-btn
    @click="router.push({ name: 'Catalog' })"
    color="primary"
    variant="elevated"
  >
    Back to catalog
  </v-btn>

  <div class="product">
    <div class="product-image">
      <img :src="selectedProduct.photoUrl" alt="" />
    </div>
    <div class="product-details">
      <p>Title: {{ selectedProduct.title }}</p>
      <p>Description: {{ selectedProduct.description }}</p>
      <h2>
        <template v-if="hasActivePromotion">
          <span class="original-price">$ {{ selectedProduct.price }}</span>
          <span class="discounted-price">$ {{ discountedPrice.toFixed(2) }}</span>
        </template>
        <template v-else>
          Price: ${{ selectedProduct.price }}
        </template>
      </h2>
      <v-btn variant="elevated" color="indigo-lighten-3" @click="addToCart">
        Add to cart
      </v-btn>
    </div>
  </div>
</template>

<script>
import { defineComponent, computed, onMounted } from 'vue';
import { productsStore } from '@/stores/products';
import { useRoute, useRouter } from 'vue-router';

export default defineComponent({
  name: 'ProductDetails',
  setup() {
    const store = productsStore();
    const router = useRouter();
    const route = useRoute();

    const selectedProduct = computed(() => {
      return store.products.find(
        (item) => item.gameId === Number(route.params.id)
      );
    });

    const hasActivePromotion = computed(() => {
      return (
        selectedProduct.value &&
        selectedProduct.value.promotions &&
        selectedProduct.value.promotions.length > 0
      );
    });

    const discountedPrice = computed(() => {
      if (hasActivePromotion.value) {
        const promotion = selectedProduct.value.promotions[0]; // Assuming one promotion per game
        const discountRate = promotion.discountRate / 100;
        return selectedProduct.value.price * (1 - discountRate);
      }
      return selectedProduct.value.price;
    });

    const addToCart = () => {
      store.addToCart(selectedProduct.value);
      router.push({ name: 'CartView' });
    };

    // Ensure products are fetched in case of direct navigation
    onMounted(async () => {
      if (!store.products.length) {
        await store.fetchProductsFromDB();
      }
    });

    return {
      router,
      selectedProduct,
      hasActivePromotion,
      discountedPrice,
      addToCart,
    };
  },
});
</script>

<style scoped>
.product {
  display: flex;
  margin-top: 50px;
}

.product-image {
  margin-right: 24px;
}

.original-price {
  text-decoration: line-through;
  color: red;
  margin-right: 8px;
}

.discounted-price {
  color: green;
  font-weight: bold;
}
</style>