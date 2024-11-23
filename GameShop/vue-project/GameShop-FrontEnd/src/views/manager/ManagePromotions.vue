<!-- File: src/views/manager/ManagePromotions.vue -->

<template>
    <v-container>
      <v-row class="align-center mb-4">
        <v-col>
          <h1>Manage Promotions</h1>
        </v-col>
        <v-col class="text-right">
          <v-btn color="green" @click="showCreateDialog = true">Create Promotion</v-btn>
          <v-btn color="red darken-1" @click="showDeleteDialog = true">Delete Promotion</v-btn>
        </v-col>
      </v-row>
  
      <!-- Promotions List -->
      <v-list two-line>
        <v-list-item
          v-for="promotion in promotions"
          :key="promotion.promotionId"
          @click="editPromotion(promotion)"
          class="hoverable"
        >
          <v-list-item-content>
            <v-list-item-title>{{ promotion.description }}</v-list-item-title>
            <v-list-item-subtitle>
              Discount: {{ promotion.discountRate }}% | Start: {{ promotion.startDate }} | End: {{ promotion.endDate }}
            </v-list-item-subtitle>
          </v-list-item-content>
          <v-list-item-icon>
            <v-icon>mdi-pencil</v-icon>
          </v-list-item-icon>
        </v-list-item>
      </v-list>
  
      <!-- Create Promotion Dialog -->
      <v-dialog v-model="showCreateDialog" max-width="500px">
        <v-card>
          <v-card-title>
            <span class="headline">Create Promotion</span>
          </v-card-title>
          <v-card-text>
            <v-text-field label="Description" v-model="newPromotion.description"></v-text-field>
            <v-text-field label="Discount Rate (%)" v-model.number="newPromotion.discountRate" type="number"></v-text-field>
            <v-menu
              ref="startMenu"
              v-model="startMenu"
              :close-on-content-click="false"
              transition="scale-transition"
              offset-y
              min-width="auto"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  label="Start Date"
                  v-model="newPromotion.startDate"
                  readonly
                  v-bind="attrs"
                  v-on="on"
                ></v-text-field>
              </template>
              <v-date-picker v-model="newPromotion.startDate" @input="startMenu = false"></v-date-picker>
            </v-menu>
            <v-menu
              ref="endMenu"
              v-model="endMenu"
              :close-on-content-click="false"
              transition="scale-transition"
              offset-y
              min-width="auto"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  label="End Date"
                  v-model="newPromotion.endDate"
                  readonly
                  v-bind="attrs"
                  v-on="on"
                ></v-text-field>
              </template>
              <v-date-picker v-model="newPromotion.endDate" @input="endMenu = false"></v-date-picker>
            </v-menu>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="green darken-1" text @click="createPromotion">Create</v-btn>
            <v-btn color="grey darken-1" text @click="showCreateDialog = false">Cancel</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
  
      <!-- Edit Promotion Dialog -->
      <v-dialog v-model="showEditDialog" max-width="500px">
        <v-card>
          <v-card-title>
            <span class="headline">Edit Promotion</span>
          </v-card-title>
          <v-card-text>
            <v-text-field label="Description" v-model="selectedPromotion.description"></v-text-field>
            <v-text-field label="Discount Rate (%)" v-model.number="selectedPromotion.discountRate" type="number"></v-text-field>
            <!-- Assign Games -->
            <v-select
              label="Assign Games"
              :items="games"
              v-model="selectedPromotion.gameIds"
              multiple
              item-text="title"
              item-value="gameId"
            ></v-select>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="updatePromotion">Save</v-btn>
            <v-btn color="grey darken-1" text @click="showEditDialog = false">Cancel</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
  
      <!-- Delete Promotion Dialog -->
      <v-dialog v-model="showDeleteDialog" max-width="500px">
        <v-card>
          <v-card-title>
            <span class="headline">Delete Promotion</span>
          </v-card-title>
          <v-card-text>
            <v-select
              label="Select Promotion to Delete"
              :items="promotions"
              v-model="promotionToDeleteId"
              item-text="description"
              item-value="promotionId"
            ></v-select>
          </v-card-text>
          <v-card-text>
            <v-alert type="error" v-if="deleteError">{{ deleteError }}</v-alert>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="red darken-1" text @click="deletePromotion">Delete</v-btn>
            <v-btn color="grey darken-1" text @click="showDeleteDialog = false">Cancel</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-container>
  </template>
  
  <script>
  import { defineComponent, ref, onMounted } from 'vue';
  import axios from 'axios';
  
  export default defineComponent({
    name: 'ManagePromotions',
    setup() {
      const promotions = ref([]);
      const games = ref([]);
      const newPromotion = ref({
        description: '',
        discountRate: null,
        startDate: '',
        endDate: '',
        managerEmail: 'manager@manager.com', // Replace with actual manager email
      });
      const selectedPromotion = ref(null);
      const showCreateDialog = ref(false);
      const showEditDialog = ref(false);
      const showDeleteDialog = ref(false);
      const promotionToDeleteId = ref(null);
      const deleteError = ref('');
      const startMenu = ref(false);
      const endMenu = ref(false);
  
      const fetchPromotions = async () => {
        try {
          const response = await axios.get('http://localhost:8080/promotions');
          promotions.value = response.data.promotions;
        } catch (error) {
          console.error('Error fetching promotions:', error);
        }
      };
  
      const fetchGames = async () => {
        try {
          const response = await axios.get('http://localhost:8080/games');
          games.value = response.data.games;
        } catch (error) {
          console.error('Error fetching games:', error);
        }
      };
  
      const createPromotion = async () => {
        try {
          await axios.post('http://localhost:8080/promotions', {
            description: newPromotion.value.description,
            discountRate: newPromotion.value.discountRate,
            startDate: newPromotion.value.startDate,
            endDate: newPromotion.value.endDate,
            managerEmail: newPromotion.value.managerEmail,
          });
          showCreateDialog.value = false;
          newPromotion.value = {
            description: '',
            discountRate: null,
            startDate: '',
            endDate: '',
            managerEmail: 'manager@manager.com',
          };
          await fetchPromotions();
        } catch (error) {
          console.error('Error creating promotion:', error);
        }
      };
  
      const editPromotion = (promotion) => {
        selectedPromotion.value = { ...promotion };
        selectedPromotion.value.gameIds = promotion.games.map((game) => game.gameId);
        showEditDialog.value = true;
      };
  
      const updatePromotion = async () => {
        if (!selectedPromotion.value.promotionId) {
          console.error('No promotion selected for update.');
          return;
        }
        try {
          await axios.put(`http://localhost:8080/promotions/${selectedPromotion.value.promotionId}`, {
            description: selectedPromotion.value.description,
            discountRate: selectedPromotion.value.discountRate,
            startDate: selectedPromotion.value.startDate,
            endDate: selectedPromotion.value.endDate,
            gameIds: selectedPromotion.value.gameIds,
          });
          showEditDialog.value = false;
          await fetchPromotions();
        } catch (error) {
          console.error('Error updating promotion:', error);
        }
      };
  
      const deletePromotion = async () => {
        if (!promotionToDeleteId.value) {
          deleteError.value = 'Please select a promotion to delete.';
          return;
        }
        try {
          await axios.delete(`http://localhost:8080/promotions/${promotionToDeleteId.value}`);
          showDeleteDialog.value = false;
          promotionToDeleteId.value = null;
          deleteError.value = '';
          await fetchPromotions();
        } catch (error) {
          console.error('Error deleting promotion:', error);
          deleteError.value = 'Failed to delete promotion. Please try again.';
        }
      };
  
      onMounted(() => {
        fetchPromotions();
        fetchGames();
      });
  
      return {
        promotions,
        games,
        newPromotion,
        selectedPromotion,
        showCreateDialog,
        showEditDialog,
        showDeleteDialog,
        promotionToDeleteId,
        deleteError,
        startMenu,
        endMenu,
        fetchPromotions,
        createPromotion,
        editPromotion,
        updatePromotion,
        deletePromotion,
      };
    },
  });
  </script>
  
  <style scoped>
  .hoverable {
    cursor: pointer;
  }
  </style>
  