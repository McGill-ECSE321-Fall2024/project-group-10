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
      <v-btn :disabled="!valid" type="submit" color="primary">Update Game</v-btn>
      <v-btn @click="cancelEdit" color="error" text>Cancel</v-btn>
    </v-form>
  </v-container>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import { productsStore } from '@/stores/products';
import { useRouter, useRoute } from 'vue-router';

export default defineComponent({
  name: 'EditGame',
  setup() {
    const store = productsStore();
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
        const gameId = route.params.id;

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

        // Call the store action to update the game
        await store.updateGame(gameId, gameData);
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

    const fetchGameDetails = async () => {
      const gameId = route.params.id;

      try {
        const response = await fetch(`http://localhost:8080/games/${gameId}`);
        if (response.ok) {
          const data = await response.json();

          // Map the received data to the game object
          game.value = {
            aTitle: data.title,
            aDescription: data.description,
            aPrice: data.price,
            aGameStatus: data.gameStatus,
            aStockQuantity: data.stockQuantity,
            aPhotoUrl: data.photoUrl,
            categories: data.categories.categories || [],
            platforms: data.platforms.platforms || [],
            promotions: data.promotions.promotions || [],
          };
        } else {
          throw new Error('Game not found');
        }
      } catch (error) {
        console.error('Error fetching game details:', error);
        alert('Failed to fetch game details.');
        router.push({ name: 'ManagerDashboard' });
      }
    };

    onMounted(async () => {
      await fetchCategories();
      await fetchPlatforms();
      await fetchPromotions();
      await fetchGameDetails();
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
      cancelEdit,
    };
  },
});
</script>

<style scoped>
/* Add styles if needed */
</style>
