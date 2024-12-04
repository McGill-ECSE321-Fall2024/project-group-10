import { defineStore } from "pinia";
import { useAuthStore } from "@/stores/auth";

export const productDetailStore = defineStore("productDetail", {
  state: () => ({
    reviews: [],
  }),

  actions: {
    // Submit a new review
    async submitReview(reviewData) {
      const authStore = useAuthStore();

      if (!authStore.user || authStore.accountType !== "CUSTOMER") {
        console.warn("Reviews can only be submitted by customers.");
        return;
      }

      try {
        const response = await fetch("http://localhost:8080/reviews", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            gameRating: reviewData.gameRating,
            description: reviewData.description.trim(),
            gameId: reviewData.gameId,
            customerEmail: authStore.user.email,
          }),
        });

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`Failed to submit review. Server response: ${errorText}`);
        }

        await this.fetchReviews(reviewData.gameId);
      } catch (error) {
        console.error("Error submitting review:", error.message);
      }
    },

    async submitReply(replyData){
      console.log(replyData);
      try {
        const response = await fetch("http://localhost:8080/replies", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            replyDate: replyData.replyDate,
            description: replyData.description,
            reviewRating: replyData.reviewRating,
            reviewId: replyData.reviewId,
            managerEmail: replyData.managerEmail,
          }),
        });
      
        if (!response.ok) {
          const errorText = await response.text();
          console.error("Server error response:", errorText); // Log the exact server error
          throw new Error(`Failed to submit reply. Server response: ${errorText}`);
        }
      
      } catch (error) {
        console.error("Error submitting reply:", error.message);
      }
    },

    // Fetch reviews for a specific product
    async fetchReviews(gameId) {
      try {
        const response = await fetch(`http://localhost:8080/reviews/review/game${gameId}`);
        if (!response.ok) {
          throw new Error(`Failed to fetch reviews. Status: ${response.status}`);
        }
        const data = await response.json();

        this.reviews = data.reviews.map((review) => ({
          id: review.reviewId,
          description: review.description,
          gameRating: review.gameRating,
          reviewRating: review.rating || 0,
          customerEmail: review.customerEmail || "Anonymous",
        }));
      } catch (error) {
        console.error("Error fetching reviews:", error.message);
      }
    },

    async fetchRepliesForAllReviews(reviews) {
      try {
        // Initialize an empty object to store replies grouped by reviewId
        const repliesByReview = {};
    
        // Iterate over all reviews and fetch replies for each
        for (const review of reviews) {
          const response = await fetch(`http://localhost:8080/replies/review/reply${review.reviewId}`);
          if (!response.ok) {
            throw new Error(`Failed to fetch replies for review ID ${review.reviewId}. Status: ${response.status}`);
          }
          const data = await response.json();
    
          // Store replies under the corresponding reviewId
          repliesByReview[review.reviewId] = data.replies.map((reply) => ({
            id: reply.replyId,
            description: reply.description,
            replyDate: reply.replyDate,
            reviewRating: reply.reviewRating,
            managerEmail: reply.managerEmail,
          }));
        }
    
        // Return the structured replies
        return repliesByReview;
      } catch (error) {
        console.error("Error fetching replies:", error.message);
        return {};
      }
    },

    // Increase review rating
    async increaseRatingReview(reviewId) {
      try {
        const response = await fetch(`http://localhost:8080/reviews/review/${reviewId}/1`, {
          method: "PUT",
        });
    
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`Failed to upvote review. Server response: ${errorText}`);
        }
    
        const updatedReview = await response.json();
        const reviewIndex = this.reviews.findIndex((r) => r.id === reviewId);
        if (reviewIndex !== -1) {
          this.reviews[reviewIndex].reviewRating = updatedReview.rating;
        }
      } catch (error) {
        console.error("Error increasing review rating:", error.message);
        throw error;
      }
    },

    // Decrease review rating
    async decreaseRatingReview(reviewId) {
      try {
        const response = await fetch(`http://localhost:8080/reviews/review/${reviewId}/-1`, {
          method: "PUT",
        });
    
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`Failed to downvote review. Server response: ${errorText}`);
        }
    
        const updatedReview = await response.json();
        const reviewIndex = this.reviews.findIndex((r) => r.id === reviewId);
        if (reviewIndex !== -1) {
          this.reviews[reviewIndex].reviewRating = updatedReview.rating;
        }
      } catch (error) {
        console.error("Error decreasing review rating:", error.message);
        throw error;
      }
    },
  },
});