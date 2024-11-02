package com.mcgill.ecse321.GameShop.dto;

import com.mcgill.ecse321.GameShop.model.Account;

public class AccountResponseDto {
    private String email;
    private String username;
    private String phoneNumber;
    private String address;

    protected AccountResponseDto(){}

    public AccountResponseDto(Account account){
        this.email = account.getEmail();
        this.username = account.getUsername();
        this.phoneNumber = account.getPhoneNumber();
        this.address = account.getAddress();
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
