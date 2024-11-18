<template>
    <v-container>
      <h1>Login</h1>
      <v-form ref="form" v-model="valid" @submit.prevent="submitLogin">
        <v-text-field
          v-model="email"
          label="Email"
          :rules="[rules.required, rules.email]"
          required
        ></v-text-field>
        <v-text-field
          v-model="password"
          label="Password"
          type="password"
          :rules="[rules.required]"
          required
        ></v-text-field>
        <v-btn :disabled="!valid" type="submit" color="primary">Login</v-btn>
      </v-form>
      <p>
        Don't have an account?
        <v-btn @click="router.push({ name: 'Register' })" text>Register here</v-btn>
      </p>
  
      <h1>Account Management</h1>
  
      <!-- Create Account Section -->
      <h2>Create New Account</h2>
      <div class="account-creation">
        <select v-model="newAccount.type">
          <option value="Customer">Customer</option>
          <option value="Employee">Employee</option>
          <option value="Manager">Manager</option>
        </select>
        <v-text-field v-model="newAccount.email" label="Email" required></v-text-field>
        <v-text-field v-model="newAccount.username" label="Username" required></v-text-field>
        <v-text-field v-model="newAccount.password" label="Password" type="password" required></v-text-field>
        <v-text-field
          v-model="newAccount.phoneNumber"
          label="Phone Number"
          required
        ></v-text-field>
        <v-text-field v-model="newAccount.address" label="Address" required></v-text-field>
        <v-btn @click="createAccount" color="success">Create Account</v-btn>
        <v-btn @click="clearForm" color="error" text>Clear</v-btn>
      </div>
  
      <!-- Accounts Table -->
      <h2>Accounts</h2>
      <table class="accounts-table">
        <thead>
          <tr>
            <th>Type</th>
            <th>Email</th>
            <th>Username</th>
            <th>Password</th>
            <th>Phone Number</th>
            <th>Address</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="account in accounts" :key="account.email">
            <td>{{ account.type }}</td>
            <td>{{ account.email }}</td>
            <td>{{ account.username }}</td>
            <td>{{ account.password || 'Not Available' }}</td>
            <td>{{ account.phoneNumber }}</td>
            <td>{{ account.address }}</td>
          </tr>
        </tbody>
      </table>
    </v-container>
  </template>
  
  <script>
  import { ref } from "vue";
  import { useAuthStore } from "@/stores/auth";
  import { useRouter } from "vue-router";
  
  export default {
    name: "Login",
    setup() {
      const auth = useAuthStore();
      const router = useRouter();
  
      const email = ref("");
      const password = ref("");
      const valid = ref(false);
  
      const newAccount = ref({
        type: "Customer",
        email: "",
        username: "",
        password: "",
        phoneNumber: "",
        address: "",
      });
  
      const accounts = ref([]);
  
      const rules = {
        required: (value) => !!value || "Required.",
        email: (value) => /.+@.+\..+/.test(value) || "Invalid email.",
      };
  
      const fetchAccounts = async () => {
        try {
          const customerResponse = await fetch(
            "http://localhost:8080/account/customers"
          );
          const employeeResponse = await fetch(
            "http://localhost:8080/account/employees"
          );
          const managerResponse = await fetch(
            "http://localhost:8080/account/getmanager"
          );
  
          const customers = (await customerResponse.json()).accounts.map((acc) => ({
            ...acc,
            type: "Customer",
          }));
          const employees = (await employeeResponse.json()).employees.map((acc) => ({
            ...acc,
            type: "Employee",
          }));
          const manager = (await managerResponse.json())
            ? [{ ...await managerResponse.json(), type: "Manager" }]
            : [];
  
          accounts.value = [...customers, ...employees, ...manager];
        } catch (error) {
          console.error("Error fetching accounts:", error);
        }
      };
  
      const submitLogin = async () => {
        try {
          await auth.login(email.value, password.value);
          if (auth.user) {
            router.push({ name: "Catalog" });
          }
        } catch (error) {
          alert("Login failed: " + error.message);
        }
      };
  
      const createAccount = async () => {
        try {
          const urlMap = {
            Customer: "http://localhost:8080/account/customer",
            Employee: "http://localhost:8080/account/employee",
            Manager: "http://localhost:8080/account/manager",
          };
          const url = urlMap[newAccount.value.type];
          if (!url) throw new Error("Invalid account type.");
  
          const response = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(newAccount.value),
          });
  
          const data = await response.json();
          accounts.value.push({ ...data, type: newAccount.value.type });
          clearForm();
          alert("Account created successfully.");
        } catch (error) {
          console.error("Error creating account:", error);
          alert("Failed to create account.");
        }
      };
  
      const clearForm = () => {
        newAccount.value = {
          type: "Customer",
          email: "",
          username: "",
          password: "",
          phoneNumber: "",
          address: "",
        };
      };
  
      fetchAccounts();
  
      return {
        email,
        password,
        valid,
        rules,
        submitLogin,
        newAccount,
        accounts,
        createAccount,
        clearForm,
      };
    },
  };
  </script>
  
  <style scoped>
  .accounts-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 1em;
  }
  
  .accounts-table th,
  .accounts-table td {
    border: 1px solid #ccc;
    padding: 0.5em;
    text-align: left;
  }
  
  .account-creation {
    display: flex;
    flex-wrap: wrap;
    gap: 1em;
    margin-bottom: 2em;
  }
  </style>
  