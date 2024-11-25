
<template>
    <v-container>
      <h1>Manager Dashboard</h1>
      <v-btn color="primary" @click="router.push({ name: 'AddEmployee' })">Add New Employee</v-btn>   
      <v-btn color="primary" @click="router.push({ name: 'ListEmployees' })">View all Employees</v-btn>   
      <v-btn color="primary" @click="router.push({ name: 'ListCustomers' })">View all Customers</v-btn>   
      <v-btn color="primary" @click="router.push({ name: 'ArchiveEmployee' })">Archive Employee</v-btn>         
      <v-btn @click="router.push({ name: 'AddGame' })">Add New Game</v-btn>
      <v-btn color="primary" @click="router.push({ name: 'ManageCategories' })">Manage Categories</v-btn>
      <v-btn color="primary" @click="router.push({ name: 'ManagePlatforms' })">Manage Platforms</v-btn>
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
  </style>
  