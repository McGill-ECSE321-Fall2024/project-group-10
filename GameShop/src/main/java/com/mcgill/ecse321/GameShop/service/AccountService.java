package com.mcgill.ecse321.GameShop.service;


import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Employee;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.WishList;
import com.mcgill.ecse321.GameShop.model.Employee.EmployeeStatus;
import com.mcgill.ecse321.GameShop.repository.AccountRepository;
import com.mcgill.ecse321.GameShop.repository.CartRepository;
import com.mcgill.ecse321.GameShop.repository.CustomerRepository;
import com.mcgill.ecse321.GameShop.repository.EmployeeRepository;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;
import com.mcgill.ecse321.GameShop.repository.WishListRepository;

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
    private WishListRepository wishListRepository;

    @Autowired
    private AccountRepository accountRepository;



    @Transactional
    public Manager createManager(String email, String username, String password, String phoneNumber, String address){
        //check that only one manager can be created
        if (managerRepo.count() > 1){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("A manager already exists"));
        }

        //input validation to ensure email, username, and password are not null
        if (email == null || email.trim().isEmpty() ){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
					String.format("Email is invalid."));
        }
        if (username == null || username.trim().isEmpty()){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("Invalid username %s", username));
        }
        if (password == null || password.trim().isEmpty()){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("Invalid password"));
        }
        //find the account using the email to check if an account with that email exists
        Account account = accountRepository.findByEmail(email);
        
        if (account != null){
            throw new GameShopException(HttpStatus.NOT_FOUND,
					String.format("Account with email %s already exists.", email));
        }

        //if email is valid create the manager
        Manager manager = new Manager(email, username, password, phoneNumber, address);
        return managerRepo.save(manager);
    }

    @Transactional
    public Employee createEmployee(String email, String username, String password, String phoneNumber, String address){
        //input validation
        if (email == null || email.trim().isEmpty() ){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
					String.format("Email is invalid."));
        }
        if (username == null || username.trim().isEmpty()){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("Invalid username %s", username));
        }
        if (password == null || password.trim().isEmpty()){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("Invalid password"));
        }

        //check to see if the email already exists in the system
        Account account = accountRepository.findByEmail(email);
        if (account != null){
            throw new GameShopException(HttpStatus.NOT_FOUND,
					String.format("Account with email %s already exists.", email));
        }

        //if email does not exist, create a new employee
        Employee employee = new Employee(email, username, password, phoneNumber, address);
        return employeeRepo.save(employee);
    }

    @Transactional
    public Customer createCustomer(String email, String username, String password, String phoneNumber, String address){
        //input validation
        if (email == null || email.trim().isEmpty() ){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
					String.format("Email is invalid."));
        }
        if (username == null || username.trim().isEmpty()){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("Invalid username %s", username));
        }
        if (password == null || password.trim().isEmpty()){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("Invalid password"));
        }

        //check if the account already exists
        Account account = accountRepository.findByEmail(email);
        if (account != null){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("Account with email %s already exists.", email));
        }

        //a cart is mandatory when creating a customer
        Cart cart = new Cart();
        cartRepository.save(cart);

        //create the customer
        Customer customer = new Customer(email, username, password, phoneNumber, address, cart);
        Customer savedCustomer = customerRepo.save(customer);
        WishList wishList = new WishList("customer", savedCustomer);
        wishListRepository.save(wishList);
        return savedCustomer;
    }



    @Transactional
    public Iterable<Account> getAllEmployees(){
        return employeeRepo.findAll();
    }


    @Transactional
    public Iterable<Account> getAllCustomers(){
        return customerRepo.findAll();
    }

    @Transactional
    public Manager getManager(){

        Iterable<Account> accounts = accountRepository.findAll();
        for (Account account: accounts){
            if (account instanceof Manager){
                return (Manager) account;
            }
        }
        throw new GameShopException(HttpStatus.NOT_FOUND, "Manager does not exist");
    }


    @Transactional
    public Account updateAccount(String email, String username, String password, String phoneNumber, String address){
        Account account = getAccountByEmail(email);
        //ensure that the account exists
        if (account!= null){
            account.setUsername(username);
            account.setPassword(password);
            account.setPhoneNumber(phoneNumber);
            account.setAddress(address);
        } else{
            throw new GameShopException(HttpStatus.NOT_FOUND,
            String.format("Account with email %s does not exist.", email));
        }
        
        return accountRepository.save(account);
    }

    @Transactional
    public Account getAccountByEmail(String email){
        //ensure the account exists
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
			throw new GameShopException(HttpStatus.NOT_FOUND,
					String.format("There is no account with email %s.", email));
		}
		return account;
    }

    @Transactional
    public Employee getEmployeeAccountByEmail(String email){
        //ensure the employee account exists
        Employee account = employeeRepo.findByEmail(email);
        if (account == null) {
			throw new GameShopException(HttpStatus.NOT_FOUND,
					String.format("There is no employee account with email %s.", email));
		}
		return account;
    }

    @Transactional
    public Customer getCustomerAccountByEmail(String email){
        //ensure the customer account exists
        Customer account = customerRepo.findByEmail(email);
        if (account == null) {
			throw new GameShopException(HttpStatus.NOT_FOUND,
					String.format("There is no customer account with email %s.", email));
		}
		return account;
    }

    @Transactional
    public Employee archiveEmployee(String email){
        //find the employee account using the email
        Employee employee = employeeRepo.findByEmail(email);
        if (employee != null){
            //set the status of the employee to archived
            employee.setEmployeeStatus(EmployeeStatus.Archived);
            return employeeRepo.save(employee);
        }else{
            throw new GameShopException(HttpStatus.NOT_FOUND,
            String.format("Account with email %s does not exist.", email));
        }
    }

    @Transactional
    public WishList getWishlistByCustomerEmail(String email){
        //find the customer account using the email
        Customer customer = customerRepo.findByEmail(email);
        WishList wishlistToReturn = null;
        if (customer == null){
            throw new GameShopException(HttpStatus.NOT_FOUND,
            String.format("Account with email %s does not exist.", email));
        } else{
            Iterable<WishList> wishlists = wishListRepository.findAll();
            for (WishList wishlist: wishlists){
            if (wishlist.getCustomer().getEmail().equals(email)){
                wishlistToReturn = wishlist;
            }
        }
        }
                return wishlistToReturn;
    }
}
