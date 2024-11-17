<template>
    <main>
      <h1>Account Management</h1>
  
      <!-- Account Form -->
      <h2>Create New Account</h2>
      <div>
        <select v-model="newAccount.type">
          <option value="Customer">Customer</option>
          <option value="Employee">Employee</option>
          <option value="Manager">Manager</option>
        </select>
        <input v-model="newAccount.email" type="email" placeholder="Email" />
        <input v-model="newAccount.username" type="text" placeholder="Username" />
        <input v-model="newAccount.password" type="password" placeholder="Password" />
        <input v-model="newAccount.phoneNumber" type="text" placeholder="Phone Number" />
        <input v-model="newAccount.address" type="text" placeholder="Address" />
        <button @click="createAccount">Create Account</button>
        <button class="danger-btn" @click="clearForm">Clear</button>
      </div>
  
      <!-- Accounts Table -->
      <h2>Accounts</h2>
      <table>
        <tbody>
          <tr>
            <th>Type</th>
            <th>Email</th>
            <th>Username</th>
            <th>Phone Number</th>
            <th>Address</th>
          </tr>
          <tr v-for="account in accounts" :key="account.email">
            <td>{{ account.type }}</td>
            <td>{{ account.email }}</td>
            <td>{{ account.username }}</td>
            <td>{{ account.phoneNumber }}</td>
            <td>{{ account.address }}</td>
          </tr>
        </tbody>
      </table>

    </main>


  </template>
  
  <script>
    import axios from "axios";
    
    export default {
        name: "AccountManagement",
        data() {
        return {
            newAccount: {
            type: "Customer",
            email: "",
            username: "",
            password: "",
            phoneNumber: "",
            address: "",
            },
            accounts: [], // List of accounts
        };
        },
        methods: {
        // Fetch accounts from the backend
        async fetchAccounts() {
            try {
            const customerResponse = await axios.get("/account/customers");
            const employeeResponse = await axios.get("/account/employees");
            const managerResponse = await axios.get("/account/getmanager");
    
            // Combine results into one array with type info
            this.accounts = [
                ...customerResponse.data.accounts.map((acc) => ({ ...acc, type: "Customer" })),
                ...employeeResponse.data.employees.map((acc) => ({ ...acc, type: "Employee" })),
                ...(managerResponse.data ? [{ ...managerResponse.data, type: "Manager" }] : []),
            ];
            } catch (error) {
            console.error("Error fetching accounts:", error);
            }
        },
    
        // Create a new account based on type
        async createAccount() {
            try {
                const urlMap = {
                Customer: "http://localhost:8080/account/customer",
                Employee: "http://localhost:8080/account/employee",
                Manager: "http://localhost:8080/account/manager",
                };
            const url = urlMap[this.newAccount.type];
    
            if (!url) {
                alert("Invalid account type!");
                return;
            }

            console.log("Error Is Being Caught Here")
            console.log("Type: ", this.newAccount.type)
            console.log("URL: ", url)
            console.log("Email: ", this.newAccount.email)
            console.log("Username: ", this.newAccount.username)
            console.log("Password: ", this.newAccount.password)
            console.log("Phone Number: ", this.newAccount.phoneNumber)
            console.log("Address: ", this.newAccount.address)
    
            const response = await axios.post(url, {
                email: this.newAccount.email,
                username: this.newAccount.username,
                password: this.newAccount.password,
                phoneNumber: this.newAccount.phoneNumber,
                address: this.newAccount.address,
            });

            console.log("HELLOOOOO")
            console.log(response.data);
    
            // Add the new account to the table
            this.accounts.push({ ...response.data, type: this.newAccount.type });
            this.clearForm(); // Clear the form after successful creation
            } catch (error) {
            console.error("Error creating account:", error);
            alert("Failed to create accountttttt.");
            }
        },
    
        // Clear the account creation form
        clearForm() {
            this.newAccount = {
            type: "Customer",
            email: "",
            username: "",
            password: "",
            phoneNumber: "",
            address: "",
            };
        },
        },
        mounted() {
        // Fetch accounts when the component is mounted
        this.fetchAccounts();
        },
    };
  </script>
  
  <style>
  main {
    display: flex;
    flex-direction: column;
    align-items: stretch;
  }
  
  h2 {
    padding-top: 1em;
    text-decoration: underline;
  }
  
  table {
    border-collapse: collapse;
  }
  
  td,
  th {
    border: 1px solid var(--color-border);
    padding: 0.25em;
  }
  
  .danger-btn {
    border: 1px solid red;
    color: red;
  }
  </style>