// File: src/stores/wishlist.js

import { defineStore } from "pinia";
import { useAuthStore } from "@/stores/auth";
import { useCartStore } from "@/stores/cart";

export const useWishlistStore = defineStore("wishlist", {
  state: () => ({
    wishlistId: null,
    wishlistItems: [],
  }),

  actions: {
    async fetchWishlist() {
      const authStore = useAuthStore();
      if (!authStore.user || authStore.accountType !== "CUSTOMER") {
        console.warn("Wishlist can only be fetched for customers.");
        return;
      }

      try {
        const response = await fetch(
          `http://localhost:8080/wishlist/customer/${authStore.user.email}`
        );

        if (!response.ok) {
          throw new Error("Failed to fetch wishlist.");
        }

        const data = await response.json();

        // Extract wishlistId and games
        this.wishlistId = data.wishlistId;
        this.wishlistItems = data.games.games.map((game) => ({
          gameId: game.gameId,
          title: game.title,
          price: game.price,
          photoUrl: game.photoUrl,
          description: game.description,
          // Add other fields if necessary
        }));
      } catch (error) {
        console.error("Error fetching wishlist:", error);
      }
    },

    async addGameToWishlist(gameId) {
      if (!this.wishlistId) {
        await this.fetchWishlist();
      }

      try {
        const response = await fetch(
          `http://localhost:8080/wishlist/${this.wishlistId}/${gameId}`,
          {
            method: "PUT",
          }
        );

        if (!response.ok) {
          throw new Error("Failed to add game to wishlist.");
        }

        await this.fetchWishlist(); // Refresh the wishlist after adding
        alert("Game added to WishList!");
      } catch (error) {
        console.error("Error adding game to wishlist:", error);
      }
    },

    async removeGameFromWishlist(gameId) {
      if (!this.wishlistId) {
        await this.fetchWishlist();
      }

      try {
        const response = await fetch(
          `http://localhost:8080/wishlist/${this.wishlistId}/${gameId}`,
          {
            method: "DELETE",
          }
        );

        if (!response.ok) {
          throw new Error("Failed to remove game from wishlist.");
        }

        await this.fetchWishlist(); // Refresh the wishlist after removing
      } catch (error) {
        console.error("Error removing game from wishlist:", error);
      }
    },

    async clearWishlist() {
      if (!this.wishlistId) {
        await this.fetchWishlist();
      }

      try {
        const response = await fetch(
          `http://localhost:8080/wishlist/${this.wishlistId}`,
          {
            method: "PUT",
          }
        );

        if (!response.ok) {
          throw new Error("Failed to clear wishlist.");
        }

        await this.fetchWishlist(); // Refresh the wishlist after clearing
      } catch (error) {
        console.error("Error clearing wishlist:", error);
      }
    },

    async addWishlistToCart() {
      const cartStore = useCartStore();
      try {
        for (const item of this.wishlistItems) {
          await cartStore.addGameToCart(item.gameId, 1);
        }
        await this.clearWishlist();
        alert("WishList moved to Cart!");
      } catch (error) {
        console.error("Error moving wishlist to cart:", error);
      }
    },
  },
});
