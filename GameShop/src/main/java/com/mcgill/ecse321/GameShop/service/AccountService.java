package com.mcgill.ecse321.GameShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Employee;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.repository.AccountRepository;
import com.mcgill.ecse321.GameShop.repository.CartRepository;
import com.mcgill.ecse321.GameShop.repository.CustomerRepository;
import com.mcgill.ecse321.GameShop.repository.EmployeeRepository;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService {
    
    @Autowired
    private ManagerRepository managerRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public Manager createManager(String email, String username, String password, String phoneNumber, String address){
        Manager manager = new Manager(email, username, password, phoneNumber, address);
        return managerRepo.save(manager);
    }

    @Transactional
    public Employee createEmployee(String email, String username, String password, String phoneNumber, String address){
        Employee employee = new Employee(email, username, password, phoneNumber, address);
        return employeeRepo.save(employee);
    }

    @Transactional
    public Customer createCustomer(String email, String username, String password, String phoneNumber, String address, Cart cart){
        Customer customer = new Customer(email, username, password, phoneNumber, address, cart);
        
        return customerRepo.save(customer);
    }

    // public List<Employee> getEmployeeList(){

    // }

    public Account getWithEmail(String email){
        Account account = accountRepository.findByEmail(email);
        // if (account == null) {
		// 	throw new EventRegistrationException(HttpStatus.NOT_FOUND,
		// 			String.format("There is no account with email %s.", email));
		// }
		return account;
    }
}
