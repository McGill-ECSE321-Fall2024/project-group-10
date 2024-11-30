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
      <h2>Price: ${{ selectedProduct.price }}</h2>
      <v-btn variant="elevated" color="indigo-lighten-3" @click="addToCart">
        Add to cart
      </v-btn>
    </div>
  </div>
</template>

<script>
import { defineComponent } from "vue";
import { computed } from "vue";
import { productsStore } from "@/stores/products";
import { useRoute, useRouter } from "vue-router";
import { useCartStore } from "@/stores/cart";
import { useAuthStore } from "@/stores/auth";

export default defineComponent({
  name: "ProductDetails",
  setup() {
    const store = productsStore();
    const cartStore = useCartStore();
    const router = useRouter();
    const route = useRoute();
    const authStore = useAuthStore();

    const isCustomer = computed(() => authStore.accountType === "CUSTOMER");
    const selectedProduct = computed(() => {
      return store.products.find(
        (item) => item.gameId === Number(route.params.id)
      );
    });

    const addToCart = async () => {
      await cartStore.addGameToCart(selectedProduct.value.gameId, 1);
      router.push({ name: "CartView" });
    };

    return {
      router,
      selectedProduct,
      addToCart,
      isCustomer,
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
</style>
