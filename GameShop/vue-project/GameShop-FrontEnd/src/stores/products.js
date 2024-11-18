import { defineStore } from 'pinia';

export const productsStore = defineStore('products', {
  state: () => ({
    products: [],
    cart: [],
  }),

  actions: {
    async fetchProductsFromDB() {
      try {
        const response = await fetch('http://localhost:8080/games');
        const data = await response.json();
        // Map games to ensure categories and platforms are arrays
        this.products = data.games.map((game) => ({
          ...game,
          categories: game.categories || [],
          platforms: game.platforms || [],
          promotions: game.promotions || [],
        }));
      } catch (error) {
        console.error('Error fetching products:', error);
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
        // Assuming data contains cart information with games and quantities
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
        // Update cart based on response
        this.cart = data.games;
      } catch (error) {
        console.error('Error adding game to cart:', error);
      }
    },

    // Additional methods for manager actions
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
        return data.categories;
      } catch (error) {
        console.error('Error fetching categories:', error);
        return [];
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
  },
});
