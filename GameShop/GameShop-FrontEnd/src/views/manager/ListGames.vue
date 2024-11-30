<template>
    <v-container>
      <h2>All Games</h2>
      <v-list>
        <v-list-item
          v-for="game in games"
          :key="game.gameId"
          @click="editGame(game.gameId)"
          class="hoverable"
        >
          <div>
            <strong>{{ game.title }}</strong><br />
            <span>{{ game.description }}</span>
          </div>
          <v-icon>mdi-pencil</v-icon>
        </v-list-item>
      </v-list>
    </v-container>
  </template>
  
  <script>
  import { defineComponent, ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { VContainer, VList, VListItem, VIcon } from 'vuetify/components';
  
  export default defineComponent({
    name: 'ListGames',
    components: {
      VContainer,
      VList,
      VListItem,
      VIcon,
    },
    setup() {
      const router = useRouter();
      const games = ref([]);
  
      const fetchGames = async () => {
        try {
          const response = await fetch('http://localhost:8080/games');
          const data = await response.json();
          console.log('Fetched games:', data); // Debugging log
          games.value = data.games || []; // Adjust based on your API response
        } catch (error) {
          console.error('Error fetching games:', error);
        }
      };
  
      const editGame = (gameId) => {
        router.push({ name: 'EditGame', params: { id: gameId } });
      };
  
      onMounted(() => {
        fetchGames();
      });
  
      return {
        games,
        editGame,
      };
    },
  });
  </script>
  
  <style scoped>
  .hoverable {
    cursor: pointer;
  }
  </style>