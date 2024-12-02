<template>
    <v-container>
      <h1>Employee Dashboard</h1>
      <v-btn color="primary" @click="router.push({ name: 'ViewEmployees' })">View all Employees</v-btn>   
      <v-btn color="primary" @click="router.push({ name: 'ViewCustomers' })">View all Customers</v-btn>
      <v-btn color="primary" @click="router.push({ name: 'ViewGameQuantity' })">View Game Quantities</v-btn>
      <v-btn color="primary" @click="router.push({ name: 'SuggestGames' })">Suggest Games</v-btn>
      <v-btn color="primary" @click="router.push({ name: 'RequestsGames' })">Request Addition of Game</v-btn>
      <router-view></router-view>
    </v-container>
  </template>
  
  <script>
  import { defineComponent } from 'vue';
  import { useRouter } from 'vue-router';
  
  export default defineComponent({
    name: 'EmployeeDashboard',
    setup() {
      const router = useRouter();
  
      return {
        router,
      };
    },
  });
  </script>
  
  <style scoped>
  </style>
  