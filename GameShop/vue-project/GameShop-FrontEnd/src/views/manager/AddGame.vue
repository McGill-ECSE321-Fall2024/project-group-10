<!-- File: src/views/manager/AddGame.vue -->

<template>
  <v-container>
    <h2>Add New Game</h2>
    <v-form ref="form" v-model="valid" @submit.prevent="submitForm">
      <!-- Existing fields -->
      <!-- ... -->
      <!-- Promotions Selection -->
      <v-select
        v-model="game.promotions"
        :items="promotions"
        label="Promotions"
        multiple
        item-text="description"
        item-value="promotionId"
      ></v-select>
      <!-- Submit Button -->
      <v-btn :disabled="!valid" type="submit" color="success">Add Game</v-btn>
      <v-btn @click="clearForm" color="error" text>Clear</v-btn>
    </v-form>
  </v-container>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import { productsStore } from '@/stores/products';
import { useRouter } from 'vue-router';

export default defineComponent({
  name: 'AddGame',
  setup() {
    const store = productsStore();
    const router = useRouter();

    const game = ref({
      aTitle: '',
      aDescription: '',
      aPrice: null,
      aGameStatus: '',
      aStockQuantity: null,
      aPhotoUrl: '',
      categories: [],
      platforms: [],
      promotions: [],
    });

    const categories = ref([]);
    const platforms = ref([]);
    const promotions = ref([]);
    const gameStatuses = ref(['InStock', 'OutOfStock', 'Archived']);

    const valid = ref(false);
    const form = ref(null);

    const rules = {
      required: (value) => !!value || 'Required.',
      number: (value) => !isNaN(value) || 'Must be a number.',
    };

    const submitForm = async () => {
      try {
        await store.addGame({
          aTitle: game.value.aTitle,
          aDescription: game.value.aDescription,
          aPrice: game.value.aPrice,
          aGameStatus: game.value.aGameStatus,
          aStockQuantity: game.value.aStockQuantity,
          aPhotoUrl: game.value.aPhotoUrl,
          categories: game.value.categories,
          platforms: game.value.platforms,
          promotions: game.value.promotions,
        });
        router.push({ name: 'ManagerDashboard' });
      } catch (error) {
        console.error('Error adding game:', error);
        alert('Failed to add game.');
      }
    };

    const clearForm = () => {
      game.value = {
        aTitle: '',
        aDescription: '',
        aPrice: null,
        aGameStatus: '',
        aStockQuantity: null,
        aPhotoUrl: '',
        categories: [],
        platforms: [],
        promotions: [],
      };
    };

    const fetchCategories = async () => {
      try {
        const response = await fetch('http://localhost:8080/categories');
        const data = await response.json();
        categories.value = data.categories.map((cat) => cat.categoryName);
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    };

    const fetchPlatforms = async () => {
      try {
        const response = await fetch('http://localhost:8080/platforms');
        const data = await response.json();
        platforms.value = data.platforms.map((plat) => plat.platformName);
      } catch (error) {
        console.error('Error fetching platforms:', error);
      }
    };

    const fetchPromotions = async () => {
      try {
        const response = await fetch('http://localhost:8080/promotions');
        const data = await response.json();
        promotions.value = data.promotions;
      } catch (error) {
        console.error('Error fetching promotions:', error);
      }
    };

    onMounted(async () => {
      await fetchCategories();
      await fetchPlatforms();
      await fetchPromotions();
    });

    return {
      game,
      categories,
      platforms,
      promotions,
      gameStatuses,
      valid,
      form,
      rules,
      submitForm,
      clearForm,
    };
  },
});
</script>

<style scoped>
/* Add styles if needed */
</style>
