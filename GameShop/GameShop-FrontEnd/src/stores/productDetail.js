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
      console.log("Review data:", reviewData);
    
      // Check if the user is logged in and is a customer
      if (!authStore.user || authStore.accountType !== "CUSTOMER") {
        console.warn("Reviews can only be submitted by customers.");
        return;
      }
    
      // Map numeric rating to text-based game rating
    
      // Validate if the rating exists in the map
      //const gameRating = reviewData.rating;
      // if (!gameRating) {
      //   console.error("Invalid game rating:", reviewData.rating);
      //   throw new Error("Invalid game rating provided.");
      // }
    
      try {
        // Submit review to the backend
        const response = await fetch("http://localhost:8080/reviews", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            gameRating : reviewData.gameRating, // Use the mapped text-based game rating
            description: reviewData.description.trim(), // Trim description
            gameId: reviewData.gameId,
            customerEmail: authStore.user.email, // Add the email field
          }),
        });
    
        // Check the response
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`Failed to submit review. Server response: ${errorText}`);
        }
    
        console.log("Review submitted successfully!");
    
        // Refresh reviews after submitting a new one
        await this.fetchReviews(reviewData.gameId);
      } catch (error) {
        console.error("Error submitting review:", error.message);
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

        // Map the response to ensure the `id` field is properly included
        this.reviews = data.reviews.map((review) => ({
          id: review.id, // Map the `id` field explicitly
          description: review.description,
          gameRating: review.gameRating,
          rating: review.rating || 0, // Default to 0 if undefined
          customerEmail: review.customerEmail || "Anonymous",
        }));
      } catch (error) {
        console.error("Error fetching reviews:", error.message);
      }
    },

    async increaseRatingReview(reviewId) {
      try {
        const response = await fetch(`http://localhost:8080/reviews/review/${reviewId}/1`, {
          method: "PUT",
        });
        if (!response.ok) {
          throw new Error(`Failed to upvote review. Status: ${response.status}`);
        }

        const review = this.reviews.find((r) => r.id === reviewId);
        if (review) {
          review.rating += 1; // Update the review's rating in the local store
        }
      } catch (error) {
        console.error("Error upvoting review:", error.message);
      }
    },
    // Downvote a review
    async decreaseRating(reviewId) {
      try {
        const response = await fetch(`http://localhost:8080/reviews/review/${reviewId}/-1`, {
          method: "PUT",
        });
        if (!response.ok) {
          throw new Error(`Failed to downvote review. Status: ${response.status}`);
        }

        const review = this.reviews.find((r) => r.id === reviewId);
        if (review) {
          review.rating -= 1; // Update the review's rating in the local store
        }
      } catch (error) {
        console.error("Error downvoting review:", error.message);
      }
    },
  },
});