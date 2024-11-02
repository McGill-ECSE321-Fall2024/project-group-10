package com.mcgill.ecse321.GameShop.controller;

import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.ArrayList;

import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountListDto;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountRequestDto;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountResponseDto;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.CustomerResponseDto;
import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Employee;
import com.mcgill.ecse321.GameShop.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public AccountResponseDto createCustomer(@RequestBody AccountRequestDto customerToCreate) {
        Customer createdCustomer = accountService.createCustomer(customerToCreate.getEmail(), 
            customerToCreate.getUsername(), 
            customerToCreate.getPassword(), 
            customerToCreate.getPhoneNumber(),
             customerToCreate.getAddress());
        
        return new AccountResponseDto(createdCustomer);
    }


    @PostMapping("/account/manager")
    public AccountResponseDto createManager(@RequestBody AccountRequestDto managerToCreate) {
        Account createdManager = accountService.createManager(managerToCreate.getEmail(), 
            managerToCreate.getUsername(), 
            managerToCreate.getPassword(), 
            managerToCreate.getPhoneNumber(),
            managerToCreate.getAddress());
        
        return new AccountResponseDto(createdManager);
    }

    @PostMapping("/account/employee")
    public AccountResponseDto createEmployee(@RequestBody AccountRequestDto employeeToCreate) {
        Account createdEmployee = accountService.createManager(employeeToCreate.getEmail(), 
            employeeToCreate.getUsername(), 
            employeeToCreate.getPassword(), 
            employeeToCreate.getPhoneNumber(),
            employeeToCreate.getAddress());
        
        return new AccountResponseDto(createdEmployee);
    }

    @GetMapping("/account/{email}")
    public AccountResponseDto findPersonByEmail(@PathVariable String email) {
        Account createdAccount = accountService.getAccountByEmail(email);
        return new AccountResponseDto(createdAccount);
    }

    @GetMapping("/account/employees")
    public AccountListDto getEmployees() {
        List<AccountResponseDto> dtos = new ArrayList<AccountResponseDto>();
        for (Account e: accountService.getAllEmployees()){
            if (e instanceof Employee){
                dtos.add(new AccountResponseDto(e));
            }
            else{
                continue;
            }
        }
        return new AccountListDto(dtos);
    }

    @GetMapping("/account/customers")
    public AccountListDto getCustomers() {
        List<AccountResponseDto> dtos = new ArrayList<AccountResponseDto>();
        for (Account c: accountService.getAllCustomers()){
            if (c instanceof Customer){
                dtos.add(new AccountResponseDto(c));
            }
            else{
                continue;
            }
        }
        return new AccountListDto(dtos);
    }
    
    //need to check the put mapping
    @PutMapping("account/{email}")
    public void updateAccount(@PathVariable String email, @RequestBody AccountRequestDto updatedInformation) {
        accountService.updateAccount(email, updatedInformation.getUsername(), 
            updatedInformation.getPassword(), 
            updatedInformation.getPhoneNumber(), 
            updatedInformation.getAddress());
    }

    @DeleteMapping("account/{email}")
    public void deleteEmployeeAccount(@PathVariable String email){
        accountService.deleteEmployee(email);
    }





    
    
    
    
}