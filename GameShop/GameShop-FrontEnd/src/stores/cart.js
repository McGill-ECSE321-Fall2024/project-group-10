import { defineStore } from "pinia";
import { useAuthStore } from "@/stores/auth";
import { productsStore } from "@/stores/products";
import { usePromotionsStore } from "@/stores/promotions";

export const useCartStore = defineStore("cart", {
  state: () => ({
    cartId: null,
    cartItems: [],
    totalPrice: 0,
  }),

  actions: {
    async fetchCart(tgt_game = null) {
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
        this.cartItems = data.games.map((g) => {
          let discountedPrice = g.game.aPrice;

          if (tgt_game && tgt_game.gameId === g.game.aGame_id) {
            discountedPrice = tgt_game.price;
          }

          return {
            gameId: g.game.aGame_id,
            title: g.game.aTitle,
            price: discountedPrice ? discountedPrice : g.game.aPrice,
            photoUrl: g.game.aPhotoUrl,
            quantity: g.quantity,
          };
        });

        this.totalPrice = this.cartItems.reduce(
          (total, item) => total + item.price * item.quantity,
          0
        );
      } catch (error) {
        console.error("Error fetching cart:", error);
      }
    },

    async addGameToCart(gameId, quantity) {
      const prod = productsStore();
      const promos = usePromotionsStore().fetchValidPromotions();

      if (!this.cartId) {
        await this.fetchCart();
      }

      // Check the target game and apply promotions if any
      const tgt_game = prod.products.find((game) => game.gameId === gameId);

      if (tgt_game === undefined || tgt_game === null) {
        console.error("Unavailable game:", tgt_game);
        return;
      }

      try {
        await fetch(`http://localhost:8080/carts/${this.cartId}/games`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ gameId, quantity }),
        });
        await this.fetchCart(tgt_game);
        alert("Game added to Cart!");
      } catch (error) {
        console.error("Error adding game to cart:", error);
      }
    },

    async updateGameQuantity(gameId, quantity) {
      try {
        const response = await fetch(
          `http://localhost:8080/carts/${this.cartId}/games/${gameId}/quantity`,
          {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ gameId, quantity }),
          }
        );
        if (!response.ok) {
          alert("Failed to update game quantity, check stock availability.");
        }
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
