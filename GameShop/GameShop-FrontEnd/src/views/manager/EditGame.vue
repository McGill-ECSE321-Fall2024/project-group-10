<template>
  <v-container>
    <h2>Edit Game</h2>
    <v-form ref="form" v-model="valid" @submit.prevent="submitForm">
      <!-- Replicate form fields from AddGame.vue, pre-filled with game data -->
      <!-- For example: -->
      <v-text-field
        v-model="game.aTitle"
        label="Game Title"
        :rules="[rules.required]"
        required
      ></v-text-field>
      <!-- Add other fields similarly -->
      <v-btn :disabled="!valid" type="submit" color="success">Save Changes</v-btn>
      <v-btn @click="cancelEdit" color="error" text>Cancel</v-btn>
    </v-form>
  </v-container>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

export default defineComponent({
  name: 'EditGame',
  props: ['id'],
  setup(props) {
    const router = useRouter();
    const game = ref({});
    const valid = ref(false);
    const form = ref(null);

    const rules = {
      required: (value) => !!value || 'Required.',
      number: (value) => !isNaN(value) || 'Must be a number.',
    };

    const fetchGameDetails = async () => {
      try {
        const response = await fetch(`http://localhost:8080/games/${props.id}`);
        const data = await response.json();
        game.value = data; // Adjust based on your API response
      } catch (error) {
        console.error('Error fetching game details:', error);
      }
    };

    const submitForm = async () => {
      try {
        await fetch(`http://localhost:8080/games/${props.id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(game.value),
        });
        router.push({ name: 'ViewAllGames' });
      } catch (error) {
        console.error('Error updating game:', error);
        alert('Failed to update game.');
      }
    };

    const cancelEdit = () => {
      router.push({ name: 'ViewAllGames' });
    };

    onMounted(() => {
      fetchGameDetails();
    });

    return {
      game,
      valid,
      form,
      rules,
      submitForm,
      cancelEdit,
    };
  },
});
</script>

<style scoped>
</style>
