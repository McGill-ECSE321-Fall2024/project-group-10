import { defineStore } from 'pinia';
import axios from 'axios';

export const usePromotionsStore = defineStore('promotions', {
  state: () => ({
    promotions: [],
  }),

  actions: {
    async fetchPromotions() {
      try {
        const response = await axios.get('http://localhost:8080/promotions');
        this.promotions = response.data.promotions || [];
      } catch (error) {
        console.error('Error fetching promotions:', error);
      }
    },

    async createPromotion(promotionData) {
      try {
        await axios.post('http://localhost:8080/promotions', promotionData);
        await this.fetchPromotions();
      } catch (error) {
        console.error('Error creating promotion:', error);
        throw error;
      }
    },

    async updatePromotion(promotionId, promotionData) {
      try {
        await axios.put(
          `http://localhost:8080/promotions/${promotionId}`,
          promotionData
        );
        await this.fetchPromotions();
      } catch (error) {
        console.error('Error updating promotion:', error);
        throw error;
      }
    },

    async deletePromotion(promotionId) {
      try {
        await axios.delete(`http://localhost:8080/promotions/${promotionId}`);
        await this.fetchPromotions();
      } catch (error) {
        console.error('Error deleting promotion:', error);
        throw error;
      }
    },
  },
});