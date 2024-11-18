<!-- File: src/views/manager/ManagerDashboard.vue -->

<template>
    <v-container>
      <h1>Manager Dashboard</h1>
      <v-btn @click="router.push({ name: 'AddGame' })">Add New Game</v-btn>
      <!-- Add other manager functionalities -->
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
  