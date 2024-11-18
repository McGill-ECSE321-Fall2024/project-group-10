<template>
  <div class="catalog-container">
    <!-- Sidebar with Filters -->
    <div class="sidebar">
      <h3>Filters</h3>
      
      <!-- Promotions Filter -->
      <div class="filter-section">
        <h4>Promotions</h4>
        <v-checkbox
          label="On Sale"
          v-model="filters.onSale"
          @change="applyFilters"
        ></v-checkbox>
      </div>

      <!-- Category Filter -->
      <div class="filter-section">
        <h4>Category</h4>
        <v-checkbox
          v-for="category in categories"
          :key="category"
          :label="category"
          :value="category"
          v-model="filters.categories"
          @change="applyFilters"
        ></v-checkbox>
      </div>

      <!-- Platform Filter -->
      <div class="filter-section">
        <h4>Platform</h4>
        <v-checkbox
          v-for="platform in platforms"
          :key="platform"
          :label="platform"
          :value="platform"
          v-model="filters.platforms"
          @change="applyFilters"
        ></v-checkbox>
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
import { defineComponent, ref, computed, onMounted } from 'vue';
import ProductItem from '@/components/ProductItem.vue';
import { productsStore } from '@/stores/products';
import { useRouter } from 'vue-router';

export default defineComponent({
  name: 'CatalogView',
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
          matches = matches && product.promotions && product.promotions.length > 0;
        }

        // Filter by Categories
        if (filters.value.categories.length > 0) {
          matches = matches && product.categories && product.categories.some((cat) =>
            filters.value.categories.includes(cat.categoryName)
          );
        }

        // Filter by Platforms
        if (filters.value.platforms.length > 0) {
          matches = matches && product.platforms && product.platforms.some((plat) =>
            filters.value.platforms.includes(plat.platformName)
          );
        }

        return matches;
      });
    });

    const applyFilters = () => {
      // Trigger computed property recalculation
    };

    const goToProductPage = (id) => {
      router.push({ name: 'ProductView', params: { id } });
    };

    onMounted(async () => {
      await store.fetchProductsFromDB();

      // Initialize categories and platforms
      categories.value = [
        ...new Set(
          store.products.flatMap((p) => (p.categories || []).map((cat) => cat.categoryName))
        ),
      ];
      platforms.value = [
        ...new Set(
          store.products.flatMap((p) => (p.platforms || []).map((plat) => plat.platformName))
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
</style>
