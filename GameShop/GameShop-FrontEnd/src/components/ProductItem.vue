<template>
  <v-card class="product-item" @click="goToProductPage(props.productData.gameId)">
    <v-img :src="props.productData.photoUrl" height="200px" cover />
    <v-card-title>{{ props.productData.title }}</v-card-title>
    <v-card-subtitle>$ {{ props.productData.price }}</v-card-subtitle>
    <v-card-text>{{ props.productData.description }}</v-card-text>
    <v-card-actions>
      <v-btn @click.stop="addToCart(props.productData)">Add to cart</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';
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
</script>

<style scoped>
.product-item {
  cursor: pointer;
}
</style>
