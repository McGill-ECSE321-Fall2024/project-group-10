package com.mcgill.ecse321.GameShop.controller;

import org.springframework.web.bind.annotation.RestController;

import com.mcgill.ecse321.GameShop.dto.AccountRequestDto;
import com.mcgill.ecse321.GameShop.dto.AccountResponseDto;
import com.mcgill.ecse321.GameShop.dto.CustomerRequestDto;
import com.mcgill.ecse321.GameShop.dto.CustomerResponseDto;
import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/account/customer")
    public CustomerResponseDto createCustomer(@RequestBody CustomerRequestDto customerToCreate) {
        Customer createdCustomer = accountService.createCustomer(customerToCreate.getEmail(), 
            customerToCreate.getUsername(), 
            customerToCreate.getPassword(), 
            customerToCreate.getPhoneNumber(),
             customerToCreate.getAddress(), customerToCreate.geCart());
        
        return new CustomerResponseDto(createdCustomer);
    }


    @PostMapping("/account/manager")
    public AccountResponseDto createManager(@RequestBody AccountRequestDto accountToCreate) {
        Account createdManager = accountService.createManager(accountToCreate.getEmail(), 
            accountToCreate.getUsername(), 
            accountToCreate.getPassword(), 
            accountToCreate.getPhoneNumber(),
            accountToCreate.getAddress());
        
        return new AccountResponseDto(createdManager);
    }

    @PostMapping("/account/employee")
    public AccountResponseDto createEmployee(@RequestBody AccountRequestDto accountToCreate) {
        Account createdEmployee = accountService.createManager(accountToCreate.getEmail(), 
            accountToCreate.getUsername(), 
            accountToCreate.getPassword(), 
            accountToCreate.getPhoneNumber(),
            accountToCreate.getAddress());
        
        return new AccountResponseDto(createdEmployee);
    }

    @GetMapping("/account/{email}")
    public AccountResponseDto findPersonByEmail(@PathVariable String email) {
        Account createdAccount = accountService.findAccountByEmail(email);
        return new AccountResponseDto(createdAccount);
    }

    // @GetMapping("/account/employees")
    // public String getMethodName(@RequestParam String param) {
    //     return new String();
    // }
    
    // @PutMapping("account/{email}")
    // public String putMethodName(@PathVariable String email, @RequestBody String entity) {
    //     //TODO: process PUT request
        
    //     return entity;
    // }


    
    
    
    
}
