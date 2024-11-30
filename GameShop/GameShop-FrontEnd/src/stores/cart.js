import { defineStore } from "pinia";
import { useAuthStore } from "@/stores/auth";

export const useCartStore = defineStore("cart", {
  state: () => ({
    cartId: null,
    cartItems: [],
    totalPrice: 0,
  }),

  actions: {
    async fetchCart() {
      const auth = useAuthStore();

      // Check if the user is logged in and is a customer
      if (!auth.user || auth.accountType !== "CUSTOMER") {
        console.warn("Cart can only be fetched for customers.");
        return;
      }

      try {
        const response = await fetch(
          `http://localhost:8080/carts/customer/${auth.user.email}`
        );
        const data = await response.json();

        this.cartId = data.cartId;
        this.cartItems = data.games.map((g) => ({
          gameId: g.game.aGame_id,
          title: g.game.aTitle,
          price: g.game.aPrice,
          photoUrl: g.game.aPhotoUrl,
          quantity: g.quantity,
        }));
        this.totalPrice = data.totalPrice;
      } catch (error) {
        console.error("Error fetching cart:", error);
      }
    },

    async addGameToCart(gameId, quantity) {
      if (!this.cartId) {
        await this.fetchCart();
      }
      try {
        await fetch(`http://localhost:8080/carts/${this.cartId}/games`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ gameId, quantity }),
        });
        await this.fetchCart();
      } catch (error) {
        console.error("Error adding game to cart:", error);
      }
    },

    async updateGameQuantity(gameId, quantity) {
      try {
        await fetch(
          `http://localhost:8080/carts/${this.cartId}/games/${gameId}/quantity`,
          {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ gameId, quantity }),
          }
        );
        await this.fetchCart();
      } catch (error) {
        console.error("Error updating game quantity:", error);
      }
    },

    async removeGameFromCart(gameId, quantity) {
      try {
        await fetch(`http://localhost:8080/carts/${this.cartId}/games/remove`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ gameId, quantity }),
        });
        await this.fetchCart();
      } catch (error) {
        console.error("Error removing game from cart:", error);
      }
    },

    async clearCart() {
      try {
        await fetch(`http://localhost:8080/carts/${this.cartId}/clear`, {
          method: "PUT",
        });
        await this.fetchCart();
      } catch (error) {
        console.error("Error clearing cart:", error);
      }
    },
  },
});
