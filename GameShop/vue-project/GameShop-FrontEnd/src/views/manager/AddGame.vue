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
      <!-- Categories Selection -->
      <v-select
        v-model="game.categories"
        :items="categories"
        label="Categories"
        multiple
        item-text="categoryName"
        item-value="categoryId"
        :rules="[rules.required]"
        required
      ></v-select>
      <!-- Platforms Selection -->
      <v-select
        v-model="game.platforms"
        :items="platforms"
        label="Platforms"
        multiple
        item-text="platformName"
        item-value="platformId"
        :rules="[rules.required]"
        required
      ></v-select>
      <!-- Promotions Selection -->
      <v-select
        v-model="game.promotions"
        :items="promotions"
        label="Promotions"
        multiple
        item-text="description"
        item-value="promotionId"
      ></v-select>
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
    const gameStatuses = ref(['InStock', 'OutOfStock', 'PreOrder']);

    const valid = ref(false);
    const form = ref(null);

    const rules = {
      required: (value) => !!value || 'Required.',
      number: (value) => !isNaN(value) || 'Must be a number.',
    };

    const submitForm = async () => {
      try {
        // Prepare the data for the API call
        const gameData = {
          aTitle: game.value.aTitle,
          aDescription: game.value.aDescription,
          aPrice: game.value.aPrice,
          aGameStatus: game.value.aGameStatus,
          aStockQuantity: game.value.aStockQuantity,
          aPhotoUrl: game.value.aPhotoUrl,
          categories: game.value.categories.map((cat) => cat.categoryId),
          platforms: game.value.platforms.map((plat) => plat.platformId),
          promotions: game.value.promotions.map((promo) => promo.promotionId),
        };

        // Call the store action to add the game
        await store.addGame(gameData);
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
        categories.value = data.categories;
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    };

    const fetchPlatforms = async () => {
      try {
        const response = await fetch('http://localhost:8080/platforms');
        const data = await response.json();
        platforms.value = data.platforms;
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
