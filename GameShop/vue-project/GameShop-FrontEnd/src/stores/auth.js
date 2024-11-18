import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,
    role: null,
  }),

  actions: {
    async login(email, password) {
      try {
        // Attempt to fetch customer account
        let response = await fetch(
          `http://localhost:8080/account/customer/${email}`
        );
        if (response.ok) {
          const customer = await response.json();
          if (customer.password === password) {
            this.user = customer;
            this.role = "customer";
            console.log("Customer account found:", customer);
            return;
          }
        }

        // Attempt to fetch employee account
        response = await fetch(
          `http://localhost:8080/account/employee/${email}`
        );
        if (response.ok) {
          const employee = await response.json();
          if (employee.password === password) {
            this.user = employee;
            this.role = "employee";
            console.log("Employee account found:", employee);
            return;
          }
        }

        // Attempt to fetch manager account
        response = await fetch("http://localhost:8080/account/getmanager");
        if (response.ok) {
          const manager = await response.json();
          if (manager.email === email && manager.password === password) {
            this.user = manager;
            this.role = "manager";
            console.log("Manager account found:", manager);
            return;
          }
        }

        throw new Error("Invalid credentials or account not found.");
      } catch (error) {
        console.error("Login error:", error.message);
        this.user = null;
        this.role = null;
        throw error;
      }
    },

    async logout() {
      this.user = null;
      this.role = null;
    },

    async registerCustomer(accountData) {
      try {
        const response = await fetch(
          "http://localhost:8080/account/customer",
          {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(accountData),
          }
        );
        const data = await response.json();
        this.user = data;
        this.role = "customer";
        console.log("Customer registered successfully:", data);
      } catch (error) {
        console.error("Registration error:", error);
        throw error;
      }
    },
  },
});
