<template>
  <v-btn
    @click="router.push({ name: 'Catalog' })"
    color="primary"
    variant="elevated"
  >
    Back to catalog
  </v-btn>

  <div class="product">
    <div class="product-image">
      <img :src="selectedProduct.photoUrl" alt="" />
      <v-btn
        v-if="isCustomer"
        variant="elevated"
        color="yellow"
        class="add-review-btn"
        @click="toggleReview"
      >
        Add Review
      </v-btn>
    </div>
    <div class="product-details">
      <p>Title: {{ selectedProduct.title }}</p>
      <p>Description: {{ selectedProduct.description }}</p>
      <h2>Price: ${{ selectedProduct.price }}</h2>
      <v-btn
        v-if="isCustomer"
        variant="elevated"
        color="indigo-lighten-3"
        @click="addToCart"
      >
        Add to cart
      </v-btn>
      <v-btn
        v-if="isCustomer"
        variant="elevated"
        color="secondary"
        @click="addToWishlist"
      >
        Add to Wishlist
      </v-btn>

      <div v-if="showReviewBox" class="review-box">
        <textarea
          v-model="reviewText"
          placeholder="Write your review here..."
          rows="4"
          cols="50"
          class="custom-textarea"
        ></textarea>
        <div class="rating-container">
          <label for="rating">Rating: </label>
          <select id="rating" v-model="rating" class="custom-select">
            <option v-for="n in 5" :key="n" :value="n">{{ n }}</option>
          </select>
        </div>
        <v-btn
          variant="elevated"
          color="success"
          @click="submitReview"
        >
          Submit Review
        </v-btn>
        <v-btn
          variant="elevated"
          color="error"
          @click="cancelReview"
        >
          Cancel
        </v-btn>
      </div>
    </div>
  </div>

  <!-- Reviews Section -->
  <div class="reviews-section" v-if="reviews.length > 0">
    <h3>Customer Reviews:</h3>
    <div class="review-item" v-for="review in reviews" :key="review.id">
      <div class="review-header">
        <p>
          <strong>{{ review.customerEmail || "Anonymous" }}</strong>,
          {{ review.gameRating }}/5
        </p>
        <p>{{ review.description }}</p>
      </div>
      <div class="review-rating">
        <v-icon
          @click="increaseRating(review)"
          :class="{ disabled: review.customerEmail === loggedInUserEmail }"
          class="arrow"
        >
          mdi-arrow-up
        </v-icon>
        <p class="score">{{ review.reviewRating }}</p>
        <v-icon
          @click="decreaseRating(review)"
          :class="{ disabled: review.customerEmail === loggedInUserEmail }"
          class="arrow"
        >
          mdi-arrow-down
        </v-icon>
      </div>
    </div>
  </div>
  <div v-else>
    <p>No reviews available for this product.</p>
  </div>
</template>

<script>
import { defineComponent, onMounted, ref, computed } from "vue";
import { productsStore } from "@/stores/products";
import { useRoute, useRouter } from "vue-router";
import { useCartStore } from "@/stores/cart";
import { useAuthStore } from "@/stores/auth";
import { useWishlistStore } from "@/stores/wishlist";
import { productDetailStore } from "@/stores/productDetail";

export default defineComponent({
  name: "ProductDetails",
  setup() {
    const store = productsStore();
    const cartStore = useCartStore();
    const wishlistStore = useWishlistStore();
    const detailStore = productDetailStore();
    const router = useRouter();
    const route = useRoute();
    const authStore = useAuthStore();
    const loggedInUserEmail = computed(() => authStore.user?.email || null);

    
    const ratingEnumMap = {
      One: "1",
      Two: "2",
      Three: "3",
      Four: "4",
      Five: "5",
    };

    const isCustomer = computed(() => authStore.accountType === "CUSTOMER");
    const selectedProduct = computed(() => {
      return store.products.find(
        (item) => item.gameId === Number(route.params.id)
      );
    });

    const addToCart = async () => {
      await cartStore.addGameToCart(selectedProduct.value.gameId, 1);
      router.push({ name: "CartView" });
    };

    const addToWishlist = async () => {
      try {
        await wishlistStore.addGameToWishlist(selectedProduct.value.gameId);
      } catch (error) {
        console.error("Error adding to wishlist:", error);
      }
    };

    // Review functionality
    const showReviewBox = ref(false);
    const reviewText = ref("");
    const rating = ref(null);

    const toggleReview = () => {
      showReviewBox.value = !showReviewBox.value;
    };

    const submitReview = async () => {
      try {
        await detailStore.submitReview({
          comment: reviewText.value.trim(),
          rating: rating.value,
          gameId: selectedProduct.value.gameId,
        });
        console.log("Review submitted successfully");
        fetchReviews(); // Refresh reviews after submission
      } catch (error) {
        console.error("Error creating review:", error);
      }
      resetReviewBox();
    };

    const cancelReview = () => {
      resetReviewBox();
    };

    const resetReviewBox = () => {
      showReviewBox.value = false;
      reviewText.value = "";
      rating.value = null;
    };

    // Fetch and display reviews
    const reviews = ref([]);

    const fetchReviews = async () => {
      try {
        const response = await fetch(
          `http://localhost:8080/reviews/review/game${selectedProduct.value.gameId}`,
          { method: "GET" }
        );
        if (!response.ok) {
          throw new Error(`Failed to fetch reviews. Status: ${response.status}`);
        }
        const data = await response.json();

        // Map backend response and correctly assign reviewId
        reviews.value = data.reviews.map((review) => ({
          id: review.id, // Ensure this matches the backend field name
          description: review.description,
          gameRating: ratingEnumMap[review.gameRating] || "0",
          reviewRating: review.reviewRating || 0,
          customerEmail: review.customerEmail || "Anonymous",
        }));
        console.log("Fetched Reviews:", reviews.value);
      } catch (error) {
        console.error("Error fetching reviews:", error.message);
      }
    };

    const increaseRating = async (review) => {
      if (review.customerEmail === loggedInUserEmail.value) {
        alert("You cannot vote on your own review.");
        return;
      }
      await detailStore.upvoteReview(review.id); // Call the store's upvoteReview method
    };

    const decreaseRating = async (review) => {
      if (review.customerEmail === loggedInUserEmail.value) {
        alert("You cannot vote on your own review.");
        return;
      }
      await detailStore.downvoteReview(review.id); // Call the store's downvoteReview method
    };

    onMounted(() => {
      fetchReviews(); // Fetch reviews on component mount
    });

    return {
      router,
      selectedProduct,
      addToCart,
      isCustomer,
      addToWishlist,
      showReviewBox,
      toggleReview,
      reviewText,
      rating,
      submitReview,
      cancelReview,
      reviews,
      increaseRating,
      decreaseRating,
      loggedInUserEmail,
    };
  },
});
</script>

<style scoped>
/* General Product Section */
.product {
  display: flex;
  margin-top: 50px;
}

.product-image {
  margin-right: 24px;
  display: flex;
  flex-direction: column;
}

.add-review-btn {
  margin-top: 20px;
}

.review-box {
  margin-top: 20px;
}

.custom-textarea {
  width: 100%;
  margin-bottom: 10px;
  border: 2px solid #ccc;
  border-radius: 5px;
  padding: 8px;
  font-size: 16px;
  resize: none;
}

.rating-container {
  margin-top: 10px;
  margin-bottom: 10px;
}

.custom-select {
  width: 150px;
  height: 40px;
  font-size: 16px;
  border: 2px solid #ccc;
  border-radius: 5px;
  padding: 5px;
}

/* Reviews Section */
.reviews-section {
  margin-top: 30px;
  padding: 16px;
  border-top: 1px solid #ddd;
}

.review-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 10px;
  background-color: #f9f9f9;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.review-header {
  flex: 1;
  margin-right: 16px;
}

.review-header p:first-of-type {
  font-weight: bold;
  margin-bottom: 5px;
}

.review-header p:last-of-type {
  margin-top: 0;
}

.no-reviews {
  text-align: center;
  margin-top: 20px;
  color: gray;
  font-style: italic;
}

/* Voting System */
.review-rating {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 50px;
}

.review-rating .arrow {
  cursor: pointer;
  font-size: 1.5rem;
  color: gray;
  transition: color 0.2s;
}

.review-rating .arrow:hover {
  color: black;
}

.review-rating .score {
  font-size: 1.2rem;
  font-weight: bold;
  margin: 5px 0; /* Space between the number and the arrows */
  text-align: center;
  line-height: 1;
}
</style>