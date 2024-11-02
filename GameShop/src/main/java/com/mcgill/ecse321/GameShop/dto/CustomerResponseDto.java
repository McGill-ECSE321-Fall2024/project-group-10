package com.mcgill.ecse321.GameShop.dto;

import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Customer;

public class CustomerResponseDto {
    private String email;
    private String username;
    private String phoneNumber;
    private String address;

    protected CustomerResponseDto(){}

    public CustomerResponseDto(Customer customer){
        this.email = customer.getEmail();
        this.username = customer.getUsername();
        this.phoneNumber = customer.getPhoneNumber();
        this.address = customer.getAddress();
    }

    public String getEmail(){
        return email;
    }

    public String getUsername(){
        return username;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getAddress(){
        return address;
    }
}
