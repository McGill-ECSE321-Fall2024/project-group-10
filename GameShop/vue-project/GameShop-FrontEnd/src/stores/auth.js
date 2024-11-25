import { defineStore } from "pinia";
import { useRouter } from "vue-router";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,
    accountType: null,
  }),

  actions: {
    async login(email, password) {
      try {
        console.log("Starting login process for:", email);

        const response = await fetch("http://localhost:8080/account/login", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email, password }),
        });

        if (!response.ok) {
          throw new Error("Invalid credentials or account not found.");
        }

        const data = await response.json();
        console.log("Login successful:", data);

        this.user = { email: data.email };
        this.accountType = data.type;
      } catch (error) {
        console.error("Login error:", error.message);
        this.user = null;
        this.accountType = null;
        throw error;
      }
    },

    async registerCustomer(accountData) {
      try {
        const response = await fetch("http://localhost:8080/account/customer", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(accountData),
        });

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`Registration failed: ${errorText}`);
        }

        const data = await response.json();
        console.log("Customer registration successful:", data);

        return data; // Return the registered account data
      } catch (error) {
        console.error("Registration error:", error.message);
        throw error;
      }
    },
    logout(router) {
      console.log("Logging out...");
    
      // Clear user state
      this.user = null;
      this.accountType = null;
    
      // Clear persistent storage if used
      localStorage.removeItem("user");
    
      // Redirect using the router instance
      if (router) {
        router.push({ name: "Catalog" });
      } else {
        console.error("Router instance is required for navigation.");
      }
    },
  },
});