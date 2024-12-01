import { defineStore } from "pinia";
import { useAuthStore } from "@/stores/auth";

export const useOrderStore = defineStore("order", {
  state: () => ({
    orders: [],
    currentOrder: null,
  }),

  actions: {
    async fetchOrders() {
      const auth = useAuthStore();

      if (!auth.user || auth.accountType !== "CUSTOMER") {
        console.warn("Orders can only be fetched for customers.");
        return;
      }

      try {
        const response = await fetch(
          `http://localhost:8080/orders/customer/${auth.user.email}`
        );

        if (!response.ok) {
          throw new Error("Failed to fetch orders.");
        }

        const data = await response.json();
        this.orders = data.orders || []; // Default to empty array if no orders
      } catch (error) {
        console.error("Error fetching orders:", error);
        this.orders = []; // Ensure the orders state remains consistent
      }
    },

    async createOrder(orderData) {
      try {
        const response = await fetch(`http://localhost:8080/orders`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(orderData),
        });

        if (!response.ok) {
          const errorText = await response.text(); // Read backend error message
          throw new Error(errorText || "Error creating order");
        }

        const newOrder = await response.json();
        this.currentOrder = newOrder;
        alert("Order successfully created!");
      } catch (error) {
        console.error("Error creating order:", error);
        alert(`Failed to create order: ${error.message}`);
        throw error;
      }
    },

    async getOrderByTrackingNumber(trackingNumber) {
      try {
        const response = await fetch(
          `http://localhost:8080/orders/${trackingNumber}`
        );

        if (!response.ok) {
          throw new Error("Error fetching order details");
        }

        const orderDetails = await response.json();
        this.currentOrder = orderDetails;
      } catch (error) {
        console.error("Error fetching order:", error);
      }
    },
  },
});
