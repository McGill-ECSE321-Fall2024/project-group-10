package com.mcgill.ecse321.GameShop.dto;

import com.mcgill.ecse321.GameShop.model.Cart;

public class CustomerRequestDto {
    private String email;
    private String username;
    private String password;
    private String phoneNumber;
    private String address;
    private Cart cart;

    public CustomerRequestDto(String email, String username, String password, String phoneNumber, String address, Cart cart){
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.cart = cart;
    }

    public String getEmail(){
        return email;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getAddress(){
        return address;
    }

    public Cart geCart(){
        return cart;
    }
}
