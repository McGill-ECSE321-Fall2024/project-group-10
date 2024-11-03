package com.mcgill.ecse321.GameShop.dto.AccountDtos;

import jakarta.validation.constraints.NotBlank;

public class AccountRequestDto {
	@NotBlank(message = "Email is required.")
    private String email;
    @NotBlank(message = "Username is required.")
    private String username;
    @NotBlank(message = "Password is required.")
    private String password;
    @NotBlank(message = "String is required.")
    private String phoneNumber;
    @NotBlank(message = "Address is required.")
    private String address;
    
    
  
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

   

    
}
