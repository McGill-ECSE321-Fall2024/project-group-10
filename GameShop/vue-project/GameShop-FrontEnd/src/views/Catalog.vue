<template>
  <div class="catalog-container">
    <!-- Sidebar with Filters -->
    <div class="sidebar">
      <h3>Filters</h3>

      <!-- Promotions Filter -->
      <div class="filter-section">
        <h4>Promotions</h4>
        <div
          class="custom-checkbox"
          v-for="(promotion, index) in [filters.onSale]"
          :key="'promotion-' + index"
        >
          <input
            type="checkbox"
            id="promotion"
            v-model="filters.onSale"
            @change="applyFilters"
          />
          <label for="promotion">On Sale</label>
        </div>
      </div>

      <!-- Category Filter -->
<!-- Category Filter -->
<div class="filter-section">
  <h4>Category</h4>
  <v-checkbox
    v-for="category in categories"
    :key="category.categoryId"
    :label="category.categoryName"
    :value="category.categoryId"
    v-model.number="filters.categories" 
    @change="applyFilters"
  ></v-checkbox>
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
            :value="platform"
            v-model="filters.platforms"
            @change="applyFilters"
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

    // Filter by Categories
    if (filters.value.categories.length > 0) {
      matches =
        matches &&
        product.categories &&
        product.categories.some((cat) =>
          filters.value.categories.includes(cat.categoryId)
        );
    }

    // Filter by Platforms (if applicable)
    if (filters.value.platforms.length > 0) {
      matches =
        matches &&
        product.platforms &&
        product.platforms.some((plat) =>
          filters.value.platforms.includes(plat.platformName)
        );
    }
    console.log("Product ID:", product.gameId);
console.log("Product Categories:", product.categories.map(cat => cat.categoryId));
console.log("Selected Categories:", filters.value.categories);
    return matches;
  });
});

    const applyFilters = () => {
      // Trigger computed property recalculation
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
  display: flex;
  align-items: center;
  justify-content: center;
}

.custom-checkbox input[type="checkbox"]:checked {
  background-color: black;
}

.custom-checkbox input[type="checkbox"]:checked::before {
  content: "✔";
  color: white;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.custom-checkbox label {
  cursor: pointer;
  font-size: 14px;
}
</style>
