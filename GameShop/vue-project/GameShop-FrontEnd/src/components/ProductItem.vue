<template>
  <v-card class="product-item" @click="goToProductPage(props.productData.gameId)">
    <v-img :src="props.productData.photoUrl" height="200px" cover />
    <v-card-title>{{ props.productData.title }}</v-card-title>
    <v-card-subtitle>
      <template v-if="hasActivePromotion">
        <span class="original-price">$ {{ props.productData.price }}</span>
        <span class="discounted-price">$ {{ discountedPrice.toFixed(2) }}</span>
      </template>
      <template v-else>
        $ {{ props.productData.price }}
      </template>
    </v-card-subtitle>
    <v-card-text>{{ props.productData.description }}</v-card-text>
    <v-card-actions>
      <v-btn @click.stop="addToCart(props.productData)">Add to cart</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup>
import { defineProps, defineEmits, computed } from 'vue';
import { productsStore } from '@/stores/products';

const props = defineProps({
  productData: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(['item-clicked']);
const store = productsStore();

const goToProductPage = (productId) => {
  emit('item-clicked', productId);
};

const addToCart = (product) => {
  store.addToCart(product);
};

const hasActivePromotion = computed(() => {
  return props.productData.promotions && props.productData.promotions.length > 0;
});

const discountedPrice = computed(() => {
  if (hasActivePromotion.value) {
    const promotion = props.productData.promotions[0]; // Assuming one promotion per game
    const discountRate = promotion.discountRate / 100;
    return props.productData.price * (1 - discountRate);
  }
  return props.productData.price;
});
</script>

<style scoped>
.product-item {
  cursor: pointer;
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
