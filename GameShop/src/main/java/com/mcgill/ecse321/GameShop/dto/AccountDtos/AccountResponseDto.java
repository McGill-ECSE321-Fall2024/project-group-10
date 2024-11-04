package com.mcgill.ecse321.GameShop.dto.AccountDtos;

import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Employee;
import com.mcgill.ecse321.GameShop.model.Manager;



public class AccountResponseDto {
    private AccountType type;
    private String email;
    private String username;
    private String phoneNumber;
    private String address;

    protected AccountResponseDto(){}

    public AccountResponseDto(Account account){
        if (account instanceof Employee){
            this.type = AccountType.EMPLOYEE;
        }
        else if (account instanceof Manager){
            this.type = AccountType.MANAGER;
        }
        else{
            this.type = AccountType.CUSTOMER;
        }
        this.email = account.getEmail();
        this.username = account.getUsername();
        this.phoneNumber = account.getPhoneNumber();
        this.address = account.getAddress();
    }

    public static AccountResponseDto create(Account account){
        if (account != null){
            return new AccountResponseDto(account);
        }
        else{
            throw new IllegalArgumentException("Account does not exist.");
        }
    }

    public AccountType getAccountType(){
        return type;
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
