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

        // Fetch specific games for each order
        const ordersWithGameDetails = await Promise.all(
          data.orders.map(async (order) => {
            try {
              const specificGamesResponse = await fetch(
                `http://localhost:8080/orders/${order.trackingNumber}/specificGames`
              );

              if (!specificGamesResponse.ok) {
                console.error(
                  `Failed to fetch specific games for order: ${order.trackingNumber}`
                );
                return { ...order, games: [] };
              }

              const specificGames = await specificGamesResponse.json();

              const gamesWithDetails = await Promise.all(
                specificGames.games.map(async (specificGame) => {
                  try {
                    console.log("SpecificGame:", specificGame);

                    const gameId =
                      specificGame.game?.aGame_id || specificGame.game?.game_id;

                    if (!gameId) {
                      console.error(
                        "Game ID not found in specificGame:",
                        specificGame
                      );
                      return { title: "Unknown Game", quantity: 1 };
                    }

                    const gameResponse = await fetch(
                      `http://localhost:8080/games/${gameId}`
                    );

                    if (!gameResponse.ok) {
                      return { title: "Unknown Game", quantity: 1 };
                    }

                    const gameDetails = await gameResponse.json();
                    return {
                      title: gameDetails.aTitle,
                      quantity: 1, // Each SpecificGame represents one unit
                    };
                  } catch (err) {
                    console.error(
                      `Error fetching game details for specificGame: ${specificGame}`,
                      err
                    );
                    return { title: "Unknown Game", quantity: 1 };
                  }
                })
              );

              return { ...order, games: gamesWithDetails };
            } catch (err) {
              console.error(`Error fetching games for order: ${order}`, err);
              return { ...order, games: [] };
            }
          })
        );

        this.orders = ordersWithGameDetails || [];
      } catch (error) {
        console.error("Error fetching orders:", error);
        this.orders = [];
      }
    },

    async createOrder(orderData) {
      try {
        // Step 1: Create the order
        const response = await fetch(`http://localhost:8080/orders`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(orderData),
        });

        if (!response.ok) {
          const errorText = await response.text();
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
