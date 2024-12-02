<template>
  <v-container>
    <h1>Order Details</h1>
    <div v-if="loading">Loading...</div>
    <div v-else-if="error">{{ error }}</div>
    <div v-else>
      <p><strong>Tracking Number:</strong> {{ order?.trackingNumber }}</p>
      <p><strong>Order Date:</strong> {{ formattedOrderDate }}</p>
      <p><strong>Items:</strong></p>
      <ul>
        <li v-for="(item, index) in order?.specificGames || []" :key="index">
          {{ item.title || "Unknown Game" }} (x{{ item.quantity || 1 }})
          <span v-if="item.trackingNumbers">
            <br />
            <strong>Tracking Numbers:</strong>
            {{ (item.trackingNumbers || []).join(", ") }}
          </span>
        </li>
      </ul>
    </div>
  </v-container>
</template>

<script>
import { defineComponent, ref, onMounted, computed } from "vue";
import { useRoute } from "vue-router";
import { useOrderStore } from "@/stores/order";

export default defineComponent({
  name: "OrderDetails",
  setup() {
    const route = useRoute();
    const orderStore = useOrderStore();
    const loading = ref(false);
    const error = ref("");
    const order = ref(null);

    const fetchOrderDetails = async () => {
      const trackingNumber = route.params.trackingNumber;
      try {
        loading.value = true;
        await orderStore.getOrderByTrackingNumber(trackingNumber);
        order.value = orderStore.currentOrder;
      } catch (err) {
        error.value = "Failed to fetch order details. Please try again.";
      } finally {
        loading.value = false;
      }
    };

    onMounted(fetchOrderDetails);

    const formattedOrderDate = computed(() => {
      return order.value?.orderDate?.split("T")[0] || "";
    });

    return {
      loading,
      error,
      order,
      formattedOrderDate,
    };
  },
});
</script>
