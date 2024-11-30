<!-- File: src/views/manager/AddGame.vue -->

<template>
  <v-container>
    <h2>Add New Game</h2>
    <v-form ref="form" v-model="valid" @submit.prevent="submitForm">
      <v-text-field
        v-model="game.aTitle"
        label="Game Title"
        :rules="[rules.required]"
        required
      ></v-text-field>
      <v-textarea
        v-model="game.aDescription"
        label="Description"
        :rules="[rules.required]"
        required
      ></v-textarea>
      <v-text-field
        v-model.number="game.aPrice"
        label="Price"
        type="number"
        :rules="[rules.required, rules.number]"
        required
      ></v-text-field>
      <v-select
        v-model="game.aGameStatus"
        :items="gameStatuses"
        label="Game Status"
        :rules="[rules.required]"
        required
      ></v-select>
      <v-text-field
        v-model.number="game.aStockQuantity"
        label="Stock Quantity"
        type="number"
        :rules="[rules.required, rules.number]"
        required
      ></v-text-field>
      <v-text-field
        v-model="game.aPhotoUrl"
        label="Photo URL"
        :rules="[rules.required]"
        required
      ></v-text-field>
      <!-- Categories and Platforms Selection -->
      <v-select
  v-model="game.categories"
  :items="categories"
  item-text="categoryName"
  item-value="categoryId" 
  label="Categories"
  multiple
  :rules="[rules.required]"
  required
></v-select>
<v-select
  v-model="game.platforms"
  :items="platforms"
  item-text="platformName"
  item-value="platformId" 
  label="Platforms"
  multiple
  :rules="[rules.required]"
  required
></v-select>
      <v-btn :disabled="!valid" type="submit" color="success">Add Game</v-btn>
      <v-btn @click="clearForm" color="error" text>Clear</v-btn>
    </v-form>
  </v-container>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import { productsStore } from '@/stores/products'; // Corrected import
import { useRouter } from 'vue-router';

export default defineComponent({
  name: 'AddGame',
  setup() {
    const store = productsStore(); // Initialize using productsStore
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
    });

    const categories = ref([]);
    const platforms = ref([]);
    const gameStatuses = ref(['InStock', 'OutOfStock', 'PreOrder']); // Example statuses

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
      };
    };

    const fetchCategories = async () => {
      try {
        const response = await fetch('http://localhost:8080/categories');
        const data = await response.json();
        return data.categories; 
      } catch (error) {
        console.error('Error fetching categories:', error);
        return [];
      }
    };

    const fetchPlatforms = async () => {
      try {
        const response = await fetch('http://localhost:8080/platforms');
        const data = await response.json();
        return data.platforms; 
      } catch (error) {
        console.error('Error fetching platforms:', error);
        return [];
      }
    };

    onMounted(async () => {
      categories.value = await fetchCategories();
      platforms.value = await fetchPlatforms();
    });

    return {
      game,
      categories,
      platforms,
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