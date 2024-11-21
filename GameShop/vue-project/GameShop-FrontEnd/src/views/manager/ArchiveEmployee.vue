<template>
    <v-container>
      <h1>Archive Employee Account</h1>
  
      <!-- Form to archive employee -->
      <v-form ref="archiveForm" v-model="formValid" lazy-validation @submit.prevent="archiveEmployee">
        <v-text-field
          v-model="email"
          label="Employee Email"
          :rules="[rules.required, rules.email]"
          required
        ></v-text-field>
        <v-btn :disabled="!formValid" type="submit" color="error">Archive Employee</v-btn>
      </v-form>
  
      <!-- Success and Error Messages -->
      <p v-if="successMessage" style="color: green;">{{ successMessage }}</p>
      <p v-if="errorMessage" style="color: red;">{{ errorMessage }}</p>
    </v-container>
  </template>
  
  <script>
  import { ref } from "vue";
  
  export default {
    name: "ArchiveEmployee",
    setup() {
      const email = ref(""); // Employee email
      const formValid = ref(false); // Form validation status
      const successMessage = ref(""); // Success message
      const errorMessage = ref(""); // Error message
  
      const rules = {
        required: (value) => !!value || "Required.",
        email: (value) => /.+@.+\..+/.test(value) || "Invalid email.",
      };
  
      // Archive employee account
      const archiveEmployee = async () => {
        try {
          // Clear previous messages
          successMessage.value = "";
          errorMessage.value = "";
  
          // Send PUT request to archive the employee
          const response = await fetch(`http://localhost:8080/account/employee/${email.value}`, {
            method: "PUT",
          });
  
          if (!response.ok) {
            // Handle not found or other errors
            if (response.status === 404) {
              throw new Error("Employee account not found.");
            } else {
              throw new Error(`Failed to archive employee. Status: ${response.status}`);
            }
          }
  
          // Success message
          successMessage.value = `Employee account with email ${email.value} has been archived successfully.`;
          email.value = ""; // Clear the email field after success
        } catch (error) {
          // Error message
          errorMessage.value = error.message;
        }
      };
  
      return {
        email,
        formValid,
        successMessage,
        errorMessage,
        rules,
        archiveEmployee,
      };
    },
  };
  </script>
  
  <style scoped>
  /* Add any necessary styles */
  </style>
  