<!-- File: src/views/manager/ManagerDashboard.vue -->

<template>
    <v-container>
      <h1>Manager Dashboard</h1>
      <v-btn color="primary" @click="router.push({ name: 'AddEmployee' })">Add New Employee</v-btn>      
      <v-btn @click="router.push({ name: 'AddGame' })">Add New Game</v-btn>
      <v-btn color="primary" @click="router.push({ name: 'ManageCategories' })">Manage Categories</v-btn>
      <v-btn color="primary" @click="router.push({ name: 'ManagePlatforms' })">Manage Platforms</v-btn>
      <!-- Add other manager functionalities -->
      
      <!-- Add router-view to render child components -->
      <router-view></router-view>
    </v-container>
  </template>
  
  <script>
  import { defineComponent } from 'vue';
  import { useRouter } from 'vue-router';
  
  export default defineComponent({
    name: 'ManagerDashboard',
    setup() {
      const router = useRouter();
  
      return {
        router,
      };
    },
  });
  </script>
  
  <style scoped>
  /* Add styles if needed */
  </style>
  