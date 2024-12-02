<template>
    <v-container>
        <h1>Contact the Manager</h1>
        <p>The Manager's email is:</p>
        <p>{{ managerEmail }}</p>
    </v-container>
    <v-btn @click="goBack">Go Back</v-btn>
</template>


<script>
import { defineComponent } from 'vue';
import { useRouter } from 'vue-router';
import { ref, onMounted } from 'vue';

export default defineComponent({
  name: 'SuggestGames',
  setup() {
    const router = useRouter();
    const games = ref([]);
    const errorMessage = ref("");
    const managerEmail = ref("");

    const fetchManagerEmail = async () => {
      try {
        // Replace with your actual API endpoint
        const response = await fetch('http://localhost:8080/account/getmanager', {
          method: 'GET',
        });

        if (!response.ok) {
          throw new Error('Failed to fetch manager email');
        }

        const data = await response.json();
        managerEmail.value = data.email;  // Assuming the response contains an 'email' field
      } catch (error) {
        errorMessage.value = 'Failed to load manager email. Please try again.';
        console.error(error);
      }
    };

    // Fetch the manager's email on component mount
    onMounted(() => {
      fetchManagerEmail();
    });

    const goBack = () => {
        router.push("/employee");
    };

    
    return {
      router,
        errorMessage,
        goBack,
        managerEmail,
    };
  },
});
</script>