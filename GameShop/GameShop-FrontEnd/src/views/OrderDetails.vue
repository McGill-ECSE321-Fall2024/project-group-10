<template>
  <v-container>
    <h1>Your Orders</h1>
    <div v-if="loading">Loading...</div>
    <div v-else-if="error">{{ error }}</div>
    <div v-else-if="!orderStore.orders.length">
      <p>No orders found for your account.</p>
      <v-btn color="primary" @click="goToCatalog">Start Shopping</v-btn>
    </div>
    <div v-else>
      <div
        v-for="order in orderStore.orders"
        :key="order.trackingNumber"
        class="order-item"
      >
        <p><strong>Tracking Number:</strong> {{ order.trackingNumber }}</p>
        <p><strong>Order Date:</strong> {{ order.orderDate }}</p>
        <p><strong>Items:</strong></p>
        <ul>
          <li v-for="(game, index) in order.games" :key="index">
            {{ game.title }} (x{{ game.quantity }})
            <span v-if="game.trackingNumbers">
              <br />
              <strong>Tracking Numbers:</strong>
              {{ game.trackingNumbers.join(", ") }}
            </span>
          </li>
        </ul>
        <v-btn color="primary" @click="viewOrder(order.trackingNumber)"
          >View Details</v-btn
        >
      </div>
    </div>
  </v-container>
</template>

<script>
import { defineComponent, onMounted, ref } from "vue";
import { useOrderStore } from "@/stores/order";
import { useRouter } from "vue-router";

export default defineComponent({
  name: "OrderHistory",
  setup() {
    const orderStore = useOrderStore();
    const router = useRouter();
    const loading = ref(false);
    const error = ref("");

    const fetchOrders = async () => {
      try {
        loading.value = true;
        await orderStore.fetchOrders();
        if (!orderStore.orders.length) {
          error.value = "No orders found for your account.";
        }
      } catch (err) {
        error.value = "Failed to fetch orders. Please try again.";
      } finally {
        loading.value = false;
      }
    };

    const goToCatalog = () => {
      router.push({ name: "Catalog" });
    };

    const viewOrder = (trackingNumber) => {
      router.push({ name: "OrderDetails", params: { trackingNumber } });
    };

    onMounted(() => {
      fetchOrders();
    });

    return {
      orderStore,
      loading,
      error,
      goToCatalog,
      viewOrder,
    };
  },
});
</script>

<style scoped>
.order-item {
  margin-bottom: 20px;
  padding: 16px;
  border: 1px solid #ccc;
  border-radius: 8px;
  background: #f9f9f9;
}
</style>
