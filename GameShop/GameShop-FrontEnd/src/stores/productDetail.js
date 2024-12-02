import { defineStore } from "pinia";
import { useAuthStore } from "@/stores/auth";

export const productDetailStore = defineStore("productDetail", {
  actions: {
    async submitReview(reviewData) {
      const authStore = useAuthStore();

      // Check if the user is logged in and is a customer
      if (!authStore.user || authStore.accountType !== "CUSTOMER") {
        console.warn("Reviews can only be submitted by customers.");
        return;
      }

      try {
        // Debugging: Log review data before sending
        console.log("Review data being sent:", {
          description: reviewData.comment.trim(), // Map 'comment' to 'description'
          gameRating: reviewData.rating, // Confirm 'rating' is sent as 'gameRating' if required
          gameId: reviewData.gameId,
          customerEmail: authStore.user.email,
        });

        // Validate client-side data
        if (!reviewData.comment.trim()) {
          console.error("Description cannot be empty.");
          throw new Error("Description cannot be empty.");
        }
        if (!reviewData.rating) {
          console.error("Game rating cannot be null.");
          throw new Error("Game rating cannot be null.");
        }

        const response = await fetch("http://localhost:8080/reviews", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            description: reviewData.comment.trim(), // Correct key for the server
            gameRating: reviewData.rating, // Confirm correct key for the server
            gameId: reviewData.gameId,
            customerEmail: authStore.user.email, // Add customer email
          }),
        });

        // Handle server response
        if (!response.ok) {
          const errorText = await response.text();
          console.error("Error response from server:", response.status, errorText);
          throw new Error(`Failed to submit review. Server response: ${errorText}`);
        }

        console.log("Review submitted successfully!");
      } catch (error) {
        console.error("Error submitting review:", error.message);
      }
    },
  },
});