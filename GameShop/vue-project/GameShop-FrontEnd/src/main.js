import { createApp } from 'vue';
import { createPinia } from 'pinia';
import './assets/styles.css';

import App from './App.vue';
import router from './router';

// Vuetify
import 'vuetify/styles';
import { createVuetify } from 'vuetify';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';

import axios from 'axios';

const vuetify = createVuetify({
  components,
  directives,
});

const app = createApp(App);

// Function to ensure the manager account exists
const ensureManagerAccount = async () => {
  try {
    // Check if the manager account already exists
    const managerResponse = await axios.get('http://localhost:8080/account/getmanager');

    if (managerResponse.data) {
      console.log('Manager account already exists:', managerResponse.data);
    } else {
      console.warn('Manager account check returned no data; proceeding to creation.');
    }
  } catch (error) {
    if (error.response && error.response.status === 404) {
      // If the manager does not exist, create it
      try {
        const creationResponse = await axios.post('http://localhost:8080/account/manager', {
          email: 'manager@manager.com',
          password: 'manager123',
          username: 'manager',
          phoneNumber: '123456',
          address: 'manager street',
        }, {
          headers: { 'Content-Type': 'application/json' },
        });

        console.log('Manager account created successfully:', creationResponse.data);
      } catch (creationError) {
        console.error('Error creating manager account:', creationError);
      }
    } else {
      console.error('Error checking manager account:', error);
    }
  }
};

// Initialize the app
(async () => {
  await ensureManagerAccount(); // Ensure manager account exists
  app.use(createPinia());
  app.use(router);
  app.use(vuetify);
  app.mount('#app');
})();
