import { defineStore } from "pinia";

export const productsStore = defineStore("products", {
  state: () => ({
    products: [],
    cart: [],
  }),

  actions: {
    async fetchProductsFromDB() {
      try {
        const response = await fetch("http://localhost:8080/games");
        const data = await response.json();
        this.products = data.games.map((game) => ({
          ...game,
          categories: game.categories?.categories || [], // Flatten categories
          platforms: game.platforms?.platforms || [], // Flatten platforms
          promotions: game.promotions || [],
        }));
      } catch (error) {
        console.error("Error fetching products:", error);
      }
    },

    async addGame(gameData) {
      let newGame = undefined;
      try {
        const response = await fetch("http://localhost:8080/games", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(gameData),
        });
        newGame = await response.json();
        this.products.push(newGame);
        console.log("Game added:", newGame);
      } catch (error) {
        console.error("Error adding game:", error);
      }

      if (newGame === undefined) {
        console.error("Error adding game:", error);
      }

      try {
        for (let i = 0; i < newGame.aStockQuantity; i++) {
          const specificGameData = {
            itemStatus: "Returned", // Adjust this status as required
            trackingNumber: [], // Add tracking numbers if needed
            game_id: newGame.aGame_id,
          };

          // Create the specific game
          const specificGameResponse = await fetch(
            "http://localhost:8080/specificGames",
            {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(specificGameData),
            }
          );
          const newSpecificGame = await specificGameResponse.json();
          if (newSpecificGame !== null) {
            console.log("Specific Game added:", newSpecificGame);
          }
        }
      } catch (error) {
        console.error("Error adding game:", error);
      }
    },

    async fetchCategories() {
      try {
        const response = await fetch("http://localhost:8080/categories");
        const data = await response.json();
        this.categories = data.categories || []; // Ensure categories are updated in the store
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    },

    async fetchPlatforms() {
      try {
        const response = await fetch("http://localhost:8080/platforms");
        const data = await response.json();
        return data.platforms;
      } catch (error) {
        console.error("Error fetching platforms:", error);
        return [];
      }
    },
  },
});
