<!-- File: src/views/manager/EditGame.vue -->

<template>
    <v-container>
      <h2>Edit Game</h2>
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
          label="Categories"
          multiple
          :rules="[rules.required]"
          required
        ></v-select>
        <v-select
          v-model="game.platforms"
          :items="platforms"
          label="Platforms"
          multiple
          :rules="[rules.required]"
          required
        ></v-select>
        <v-btn :disabled="!valid" type="submit" color="primary">Update Game</v-btn>
        <v-btn @click="cancelEdit" color="error" text>Cancel</v-btn>
      </v-form>
    </v-container>
  </template>
  
  <script>
  import { defineComponent, ref, onMounted } from 'vue';
  import { productsStore } from '@/stores/products'; // Corrected import
  import { useRouter, useRoute } from 'vue-router';
  
  export default defineComponent({
    name: 'EditGame',
    setup() {
      const store = productsStore(); // Initialize using productsStore
      const router = useRouter();
      const route = useRoute();
  
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
          const gameId = route.params.id;
          await store.updateGame(gameId, {
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
          console.error('Error updating game:', error);
          alert('Failed to update game.');
        }
      };
  
      const cancelEdit = () => {
        router.push({ name: 'ManagerDashboard' });
      };
  
      const fetchCategories = async () => {
        try {
          const response = await fetch('http://localhost:8080/categories');
          const data = await response.json();
          return data.categories.map((cat) => cat.categoryName);
        } catch (error) {
          console.error('Error fetching categories:', error);
          return [];
        }
      };
  
      const fetchPlatforms = async () => {
        try {
          const response = await fetch('http://localhost:8080/platforms');
          const data = await response.json();
          return data.platforms.map((plat) => plat.platformName);
        } catch (error) {
          console.error('Error fetching platforms:', error);
          return [];
        }
      };
  
      const fetchGameDetails = async () => {
        const gameId = route.params.id;
        // Fetch the game details from the store
        const existingGame = store.products.find((g) => g.game_id === Number(gameId));
        if (existingGame) {
          game.value = { ...existingGame };
        } else {
          // Optionally, fetch the game from the backend if not in the store
          try {
            const response = await fetch(`http://localhost:8080/games/${gameId}`);
            if (response.ok) {
              const data = await response.json();
              game.value = { ...data };
            } else {
              throw new Error('Game not found');
            }
          } catch (error) {
            console.error('Error fetching game details:', error);
            alert('Failed to fetch game details.');
            router.push({ name: 'ManagerDashboard' });
          }
        }
      };
  
      onMounted(async () => {
        await store.fetchProductsFromDB(); // Ensure products are loaded
        categories.value = await fetchCategories();
        platforms.value = await fetchPlatforms();
        await fetchGameDetails();
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
        cancelEdit,
      };
    },
  });
  </script>
  
  <style scoped>
  /* Add styles if needed */
  </style>
  