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
