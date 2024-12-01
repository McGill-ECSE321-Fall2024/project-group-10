<template>
  <v-card class="product-item" @click="goToProductPage(props.productData.gameId)">
    <v-img :src="props.productData.photoUrl" height="200px" cover />
    <v-card-title>{{ props.productData.title }}</v-card-title>

    <!-- <v-card-subtitle>$ {{ props.productData.price }}</v-card-subtitle> -->

    <!-- Display PRices with Promo Logic -->
    <v-card-subtitle>
      <!-- Show discounted price if it exists, otherwise show original price -->
      <span v-if="props.productData.originalPrice && props.productData.price !== props.productData.originalPrice">
        <span class="original-price" style="text-decoration: line-through; color: gray;">
          $ {{ props.productData.originalPrice }}
        </span>
        <span class="discounted-price" style="color: green; margin-left: 8px;">
          $ {{ props.productData.price }}
        </span>
      </span>
      <span v-else>
        $ {{ props.productData.price }}
      </span>
    </v-card-subtitle>

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

.original-price {
  text-decoration: line-through;
  color: red; /* Optional: Highlight the original price */
  margin-right: 8px; /* Add spacing between original and discounted price */
}

.discounted-price {
  color: green; /* Optional: Highlight the discounted price */
  font-weight: bold; /* Make it stand out */
}

</style>
