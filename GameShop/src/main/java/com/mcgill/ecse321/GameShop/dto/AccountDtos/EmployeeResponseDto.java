package com.mcgill.ecse321.GameShop.dto.AccountDtos;

import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Employee;
import com.mcgill.ecse321.GameShop.model.Employee.EmployeeStatus;

public class EmployeeResponseDto {
    private EmployeeStatus employeeStatus;
    private String email;
    private String username;
    private String phoneNumber;
    private String address;

    protected EmployeeResponseDto(){}

    public EmployeeResponseDto(Account account){
        if (account instanceof Employee){
           this.employeeStatus = ((Employee)account).getEmployeeStatus();
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

    public EmployeeStatus getEmployeeStatus(){
        return employeeStatus;
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
