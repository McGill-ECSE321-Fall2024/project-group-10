package com.mcgill.ecse321.GameShop.dto.AccountDtos;

public class ManagerDto {
    private String email;
    private String username;
    private String password;
    private String phoneNumber;
    private String address;

    public ManagerDto(String email, String username, String password, String phoneNumber, String address){
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
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
}
