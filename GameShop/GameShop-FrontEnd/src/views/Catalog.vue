<template>
  <div class="catalog-container">
    <!-- Sidebar with Filters -->
    <div class="sidebar">
      <h3>Filters</h3>

      <!-- Promotions Filter -->
      <div class="filter-section">
        <h4>Promotions</h4>
        <div class="custom-checkbox">
          <input
            type="checkbox"
            id="onSale"
            v-model="filters.onSale"
            @change="applyFilters"
          />
          <label for="onSale">On Sale</label>
        </div>
      </div>

      <!-- Category Filter -->
      <div class="filter-section">
        <h4>Category</h4>
        <div
          class="custom-checkbox"
          v-for="category in categories"
          :key="category.categoryId"
        >
          <input
            type="checkbox"
            :id="'category-' + category.categoryId"
            :checked="filters.categories.includes(category.categoryId)"
            @change="toggleCategoryFilter(category.categoryId)"
          />
          <label :for="'category-' + category.categoryId">
            {{ category.categoryName }}
          </label>
        </div>
      </div>

      <!-- Platform Filter -->
      <div class="filter-section">
        <h4>Platform</h4>
        <div
          class="custom-checkbox"
          v-for="platform in platforms"
          :key="platform.platformId"
        >
          <input
            type="checkbox"
            :id="'platform-' + platform.platformId"
            :checked="filters.platforms.includes(platform.platformId)"
            @change="togglePlatformFilter(platform.platformId)"
          />
          <label :for="'platform-' + platform.platformId">
            {{ platform.platformName }}
          </label>
        </div>
      </div>
    </div>

    <!-- Products List -->
    <div class="products-list">
      <v-row no-gutters>
        <v-col
          v-for="product in filteredProducts"
          :key="product.gameId"
          cols="12"
          sm="4"
        >
          <product-item
            :product-data="product"
            @item-clicked="goToProductPage"
          />
        </v-col>
      </v-row>
    </div>
  </div>
</template>

<script>
import { defineComponent, ref, computed, onMounted } from "vue";
import ProductItem from "@/components/ProductItem.vue";
import { productsStore } from "@/stores/products";
import { useRouter } from "vue-router";

export default defineComponent({
  name: "CatalogView",
  components: {
    ProductItem,
  },
  setup() {
    const store = productsStore();
    const router = useRouter();

    const filters = ref({
      onSale: false,
      categories: [],
      platforms: [],
    });

    const categories = ref([]);
    const platforms = ref([]);

    const filteredProducts = computed(() => {
      return store.products.filter((product) => {
        console.log('Product:', product);
      console.log('Product Categories:', product.categories);
      console.log('Product Platforms:', product.platforms);
        let matches = true;

        // Filter by Promotions
        if (filters.value.onSale) {
          matches =
            matches &&
            product.promotions &&
            product.promotions.length > 0;
        }

        // Since we fetch games by categories and platforms from the backend,
        // we only need to filter by promotions here.

        return matches;
      });
    });

    const applyFilters = async () => {
      if (filters.value.categories.length > 0 && filters.value.platforms.length > 0) {
        // Fetch games by categories and platforms
        await fetchGamesByCategoriesAndPlatforms(filters.value.categories, filters.value.platforms);
      } else if (filters.value.categories.length > 0) {
        await fetchGamesByCategories(filters.value.categories);
      } else if (filters.value.platforms.length > 0) {
        await fetchGamesByPlatforms(filters.value.platforms);
      } else {
        await store.fetchProductsFromDB();
      }
    };

    const fetchGamesByCategories = async (categoryIds) => {
      try {
        let gamesMap = new Map();
        for (const id of categoryIds) {
          const response = await fetch(
            `http://localhost:8080/categories/${id}/games`
          );
          const data = await response.json();

          data.games.forEach((game) => {
            gamesMap.set(game.gameId, {
              ...game,
              categories: game.categories?.categories || [],
              platforms: game.platforms?.platforms || [],
              promotions: game.promotions || [],
            });
          });
        }

        store.products = Array.from(gamesMap.values());
      } catch (error) {
        console.error("Error fetching games by categories:", error);
      }
    };

    const fetchGamesByPlatforms = async (platformIds) => {
      try {
        let gamesMap = new Map();
        for (const id of platformIds) {
          const response = await fetch(
            `http://localhost:8080/platforms/${id}/games`
          );
          const data = await response.json();

          data.games.forEach((game) => {
            gamesMap.set(game.gameId, {
              ...game,
              categories: game.categories?.categories || [],
              platforms: game.platforms?.platforms || [],
              promotions: game.promotions || [],
            });
          });
        }

        store.products = Array.from(gamesMap.values());
      } catch (error) {
        console.error("Error fetching games by platforms:", error);
      }
    };

    // const fetchGamesByCategoriesAndPlatforms = async (categoryIds, platformIds) => {
    //   try {
    //     let gamesMap = new Map();
    //     for (const catId of categoryIds) {
    //       for (const platId of platformIds) {
    //         const response = await fetch(
    //           `http://localhost:8080/games?categoryId=${catId}&platformId=${platId}`
    //         );
    //         const data = await response.json();

    //         data.games.forEach((game) => {
    //           gamesMap.set(game.gameId, {
    //             ...game,
    //             categories: game.categories?.categories || [],
    //             platforms: game.platforms?.platforms || [],
    //             promotions: game.promotions || [],
    //           });
    //         });
    //       }
    //     }

    //     store.products = Array.from(gamesMap.values());
    //   } catch (error) {
    //     console.error("Error fetching games by categories and platforms:", error);
    //   }
    // };
    const fetchGamesByCategoriesAndPlatforms = async (categoryIds, platformIds) => {
      try {
        let categoryGamesMap = new Map();
        let platformGamesMap = new Map();

        // Fetch games by categories
        for (const id of categoryIds) {
          const response = await fetch(
            `http://localhost:8080/categories/${id}/games`
          );
          const data = await response.json();

          data.games.forEach((game) => {
            categoryGamesMap.set(game.gameId, {
              ...game,
              categories: game.categories?.categories || [],
              platforms: game.platforms?.platforms || [],
              promotions: game.promotions || [],
            });
          });
        }

        // Fetch games by platforms
        for (const id of platformIds) {
          const response = await fetch(
            `http://localhost:8080/platforms/${id}/games`
          );
          const data = await response.json();

          data.games.forEach((game) => {
            platformGamesMap.set(game.gameId, {
              ...game,
              categories: game.categories?.categories || [],
              platforms: game.platforms?.platforms || [],
              promotions: game.promotions || [],
            });
          });
        }

        // Find intersection of games in both maps
        const intersectionGames = Array.from(categoryGamesMap.values()).filter((game) =>
          platformGamesMap.has(game.gameId)
        );

        store.products = intersectionGames;
      } catch (error) {
        console.error("Error fetching games by categories and platforms:", error);
      }
    };

    const toggleCategoryFilter = async (categoryId) => {
      if (filters.value.categories.includes(categoryId)) {
        filters.value.categories = filters.value.categories.filter(
          (id) => id !== categoryId
        );
      } else {
        filters.value.categories.push(categoryId);
      }
      await applyFilters();
    };

    const togglePlatformFilter = async (platformId) => {
      if (filters.value.platforms.includes(platformId)) {
        filters.value.platforms = filters.value.platforms.filter(
          (id) => id !== platformId
        );
      } else {
        filters.value.platforms.push(platformId);
      }
      await applyFilters();
    };

    const goToProductPage = (id) => {
      router.push({ name: "ProductView", params: { id } });
    };

    onMounted(async () => {
      await store.fetchProductsFromDB();

      // Fetch categories dynamically from the backend
      try {
        const response = await fetch("http://localhost:8080/categories");
        const data = await response.json();
        categories.value = data.categories.map((cat) => ({
          categoryName: cat.categoryName,
          categoryId: cat.categoryId,
        }));
      } catch (error) {
        console.error("Error fetching categories:", error);
      }

      // Fetch platforms dynamically from the backend
      try {
        const response = await fetch("http://localhost:8080/platforms");
        const data = await response.json();
        platforms.value = data.platforms.map((plat) => ({
          platformName: plat.platformName,
          platformId: plat.platformId,
        }));
      } catch (error) {
        console.error("Error fetching platforms:", error);
      }
    });

    return {
      filters,
      categories,
      platforms,
      filteredProducts,
      goToProductPage,
      applyFilters,
      toggleCategoryFilter,
      togglePlatformFilter,
    };
  },
});
</script>

<style scoped>
.catalog-container {
  display: flex;
}

/* Sidebar Styles */
.sidebar {
  width: 250px;
  padding: 16px;
  border-right: 1px solid #ccc;
}

.filter-section {
  margin-bottom: 24px;
}

/* Products List Styles */
.products-list {
  flex: 1;
  padding: 16px;
}

/* Custom Checkbox Styles */
.custom-checkbox {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.custom-checkbox input[type="checkbox"] {
  appearance: none;
  width: 20px;
  height: 20px;
  border: 2px solid black;
  border-radius: 2px;
  outline: none;
  cursor: pointer;
  margin-right: 8px;
  position: relative;
}

.custom-checkbox input[type="checkbox"]:checked {
  background-color: black;
}

.custom-checkbox input[type="checkbox"]:checked::before {
  content: "✔";
  color: white;
  font-size: 16px;
  position: absolute;
  left: 2px;
  top: -2px;
}

.custom-checkbox label {
  cursor: pointer;
  font-size: 14px;
}
</style>
