<!-- File: src/views/manager/ManageCategories.vue -->

<template>
    <v-container>
      <v-row class="align-center mb-4">
        <v-col>
          <h1>Manage Categories</h1>
        </v-col>
        <v-col class="text-right">
          <v-btn color="green" @click="showCreateDialog = true">Create Category</v-btn>
        </v-col>
      </v-row>
  
      <!-- Categories List -->
      <v-list two-line>
        <v-list-item
          v-for="category in sortedCategories"
          :key="category.categoryId"
          @click="editCategory(category)"
          class="hoverable"
        >
          <v-list-item-content>
            <v-list-item-title>{{ category.categoryName }}</v-list-item-title>
          </v-list-item-content>
          <v-list-item-icon>
            <v-icon>mdi-pencil</v-icon>
          </v-list-item-icon>
        </v-list-item>
      </v-list>
  
      <!-- Create Category Dialog -->
      <v-dialog v-model="showCreateDialog" max-width="500px">
        <v-card>
          <v-card-title>
            <span class="headline">Create Category</span>
          </v-card-title>
          <v-card-text>
            <v-text-field label="Category Name" v-model="newCategoryName"></v-text-field>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="green darken-1" text @click="createCategory">Create</v-btn>
            <v-btn color="grey darken-1" text @click="showCreateDialog = false">Cancel</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
  
      <!-- Edit Category Dialog -->
      <v-dialog v-model="showEditDialog" max-width="500px">
        <v-card>
          <v-card-title>
            <span class="headline">Edit Category</span>
          </v-card-title>
          <v-card-text>
            <v-text-field label="Category Name" v-model="selectedCategory.categoryName"></v-text-field>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="updateCategory">Save</v-btn>
            <v-btn color="grey darken-1" text @click="showEditDialog = false">Cancel</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-container>
  </template>
  
  <script>
  import { defineComponent, ref, onMounted, computed } from 'vue';
  import axios from 'axios';
  
  export default defineComponent({
    name: 'ManageCategories',
    setup() {
      const categories = ref([]);
      const newCategoryName = ref('');
      const showCreateDialog = ref(false);
      const showEditDialog = ref(false);
      const selectedCategory = ref(null);
  
      const fetchCategories = async () => {
        try {
          const response = await axios.get('http://localhost:8080/categories');
          categories.value = response.data.categories;
        } catch (error) {
          console.error('Error fetching categories:', error);
        }
      };
  
      const createCategory = async () => {
        try {
          await axios.post('http://localhost:8080/categories', {
            categoryName: newCategoryName.value,
            managerEmail: 'manager@manager.com', // Replace with actual manager email
          });
          showCreateDialog.value = false;
          newCategoryName.value = '';
          await fetchCategories();
        } catch (error) {
          console.error('Error creating category:', error);
        }
      };
  
      const editCategory = (category) => {
        selectedCategory.value = { ...category };
        showEditDialog.value = true;
      };
  
      const updateCategory = async () => {
        try {
          await axios.put(`http://localhost:8080/categories/${selectedCategory.value.categoryId}`, {
            categoryName: selectedCategory.value.categoryName,
          });
          showEditDialog.value = false;
          await fetchCategories();
        } catch (error) {
          console.error('Error updating category:', error);
        }
      };
  
      const sortedCategories = computed(() => {
        return categories.value.slice().sort((a, b) => {
          return a.categoryName.localeCompare(b.categoryName);
        });
      });
  
      onMounted(() => {
        fetchCategories();
      });
  
      return {
        categories,
        newCategoryName,
        showCreateDialog,
        showEditDialog,
        selectedCategory,
        fetchCategories,
        createCategory,
        editCategory,
        updateCategory,
        sortedCategories,
      };
    },
  });
  </script>
  
  <style scoped>
  .hoverable {
    cursor: pointer;
  }
  </style>
  