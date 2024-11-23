
import { defineStore } from "pinia";

export const productsStore = defineStore("products", {
  state: () => ({
    products: [],
    cart: [],
    categories: [],
    platforms: [],
    promotions: [],
  }),

  actions: {
    async fetchProductsFromDB() {
      try {
        const response = await fetch("http://localhost:8080/games");
        const data = await response.json();
        this.products = data.games.map((game) => ({
          ...game,
          categories: game.categories?.categories || [],
          platforms: game.platforms?.platforms || [],
          promotions: game.promotions?.promotions || [],
        }));
      } catch (error) {
        console.error("Error fetching products:", error);
      }
    },
    addToCart(product) {
      this.cart.push(product);
    },

    removeFromCart(id) {
      this.cart = this.cart.filter((item) => item.gameId !== id);
    },

    async fetchCart(customerEmail) {
      try {
        const response = await fetch(`http://localhost:8080/carts/customer/${customerEmail}`);
        const data = await response.json();
        this.cart = data.games;
      } catch (error) {
        console.error('Error fetching cart:', error);
      }
    },

    async addGameToCart(cartId, gameId, quantity) {
      try {
        const response = await fetch(`http://localhost:8080/carts/${cartId}/games`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ gameId, quantity }),
        });
        const data = await response.json();
        this.cart = data.games;
      } catch (error) {
        console.error('Error adding game to cart:', error);
      }
    },

    async addGame(gameData) {
      try {
        const response = await fetch('http://localhost:8080/games', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(gameData),
        });
        const newGame = await response.json();
        this.products.push(newGame);
      } catch (error) {
        console.error('Error adding game:', error);
      }
    },

    async fetchCategories() {
        try {
          const response = await fetch('http://localhost:8080/categories');
          const data = await response.json();
          this.categories = data.categories || []; // Ensure categories are updated in the store
        } catch (error) {
          console.error('Error fetching categories:', error);
        }
      },

    async fetchPlatforms() {
      try {
        const response = await fetch('http://localhost:8080/platforms');
        const data = await response.json();
        return data.platforms;
      } catch (error) {
        console.error('Error fetching platforms:', error);
        return [];
      }
    },

    async fetchPromotions() {
      try {
        const response = await fetch('http://localhost:8080/promotions');
        const data = await response.json();
        this.promotions = data.promotions || [];
      } catch (error) {
        console.error('Error fetching promotions:', error);
      }
    },

    async addGame(gameData) {
      try {
        const response = await fetch('http://localhost:8080/games', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(gameData),
        });
        const newGame = await response.json();
        this.products.push(newGame);
      } catch (error) {
        console.error('Error adding game:', error);
      }
    },

    async updateGame(gameId, gameData) {
      try {
        const response = await fetch(`http://localhost:8080/games/${gameId}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(gameData),
        });
        const updatedGame = await response.json();
        const index = this.products.findIndex((game) => game.gameId === gameId);
        if (index !== -1) {
          this.products.splice(index, 1, updatedGame);
        }
      } catch (error) {
        console.error('Error updating game:', error);
      }
    },
  },
});
