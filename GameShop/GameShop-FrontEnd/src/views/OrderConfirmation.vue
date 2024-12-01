<template>
  <v-container>
    <h1>Order Confirmation</h1>
    <p>Tracking Number: {{ orderStore.currentOrder.trackingNumber }}</p>
    <p>Order Date: {{ orderStore.currentOrder.orderDate }}</p>
    <p>Thank you for your purchase!</p>

    <v-btn color="primary" @click="goToCatalog">Back to Catalog</v-btn>
  </v-container>
</template>

<script>
import { defineComponent } from "vue";
import { useOrderStore } from "@/stores/order";
import { useRouter } from "vue-router";

export default defineComponent({
  name: "OrderConfirmation",
  setup() {
    const orderStore = useOrderStore();
    const router = useRouter();

    const goToCatalog = () => {
      router.push({ name: "Catalog" });
    };

    return {
      orderStore,
      goToCatalog,
    };
  },
});
</script>

<style scoped>
p {
  font-size: 1.2em;
  margin: 10px 0;
}
</style>
