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
          :key="platform"
        >
          <input
            type="checkbox"
            :id="'platform-' + platform"
            :checked="filters.platforms.includes(platform)"
            @change="togglePlatformFilter(platform)"
          />
          <label :for="'platform-' + platform">{{ platform }}</label>
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
        let matches = true;

        // Filter by Promotions
        if (filters.value.onSale) {
          matches =
            matches &&
            product.promotions &&
            product.promotions.length > 0;
        }

        // Filter by Platforms
        if (filters.value.platforms.length > 0) {
          matches =
            matches &&
            product.platforms &&
            product.platforms.some((plat) =>
              filters.value.platforms.includes(plat.platformName)
            );
        }

        return matches;
      });
    });

    const applyFilters = async () => {
      if (filters.value.categories.length > 0) {
        await fetchGamesByCategories(filters.value.categories);
      } else {
        await store.fetchProductsFromDB();
      }
    };

    const fetchGamesByCategories = async (categoryIds) => {
      try {
        let gamesSet = new Set();
        for (const id of categoryIds) {
          const response = await fetch(
            `http://localhost:8080/categories/${id}/games`
          );
          const data = await response.json();

          data.games.forEach((game) => {
            // Convert game object to string for Set uniqueness
            gamesSet.add(JSON.stringify(game));
          });
        }

        // Convert Set back to array of game objects
        const gamesArray = Array.from(gamesSet).map((gameStr) =>
          JSON.parse(gameStr)
        );

        // Process gamesArray as needed
        const games = gamesArray.map((game) => ({
          ...game,
          categories: game.categories?.categories || [],
          platforms: game.platforms?.platforms || [],
          promotions: game.promotions || [],
        }));

        store.products = games;
      } catch (error) {
        console.error("Error fetching games by categories:", error);
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

    const togglePlatformFilter = (platformName) => {
      if (filters.value.platforms.includes(platformName)) {
        filters.value.platforms = filters.value.platforms.filter(
          (name) => name !== platformName
        );
      } else {
        filters.value.platforms.push(platformName);
      }
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

      // Initialize platforms dynamically based on products
      platforms.value = [
        ...new Set(
          store.products.flatMap((p) =>
            (p.platforms || []).map((plat) => plat.platformName)
          )
        ),
      ];
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
