<template>
    <v-container>
      <v-form ref="form" v-model="valid" @submit.prevent="submitRegister">
        <v-text-field
          v-model="accountData.email"
          label="Email"
          :rules="[rules.required, rules.email]"
          required
        ></v-text-field>
        <v-text-field
          v-model="accountData.username"
          label="Username"
          :rules="[rules.required]"
          required
        ></v-text-field>
        <v-text-field
          v-model="accountData.password"
          label="Password"
          type="password"
          :rules="[rules.required]"
          required
        ></v-text-field>
        <v-text-field
          v-model="accountData.phoneNumber"
          label="Phone Number"
          :rules="[rules.required]"
          required
        ></v-text-field>
        <v-text-field
          v-model="accountData.address"
          label="Address"
          :rules="[rules.required]"
          required
        ></v-text-field>
        <v-btn :disabled="!valid" type="submit">Register</v-btn>
      </v-form>
    </v-container>
  </template>
  
  <script>
  import { defineComponent, ref } from 'vue';
  import { useAuthStore } from '@/stores/auth';
  import { useRouter } from 'vue-router';
  
  export default defineComponent({
    name: 'Register',
    setup() {
      const auth = useAuthStore();
      const router = useRouter();
  
      const accountData = ref({
        email: '',
        username: '',
        password: '',
        phoneNumber: '',
        address: '',
      });
  
      const valid = ref(false);
      const form = ref(null);
  
      const rules = {
        required: (value) => !!value || 'Required.',
        email: (value) => /.+@.+\..+/.test(value) || 'Invalid email.',
      };
  
      const submitRegister = async () => {
        await auth.registerCustomer(accountData.value);
        if (auth.user) {
          router.push({ name: 'Catalog' });
        } else {
          alert('Registration failed');
        }
      };
  
      return {
        accountData,
        valid,
        form,
        rules,
        submitRegister,
      };
    },
  });
  </script>
  
  <style scoped>
  /* Add styles if needed */
  </style>
  