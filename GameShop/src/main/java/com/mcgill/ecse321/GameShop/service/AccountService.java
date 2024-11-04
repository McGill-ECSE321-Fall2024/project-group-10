package com.mcgill.ecse321.GameShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Employee;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Employee.EmployeeStatus;
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
        if (managerRepo.count() > 1){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("A manager already exists"));
        }
        if (email.trim().isEmpty() || email == null){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
					String.format("Email is invalid"));
        }
        if (username.trim().isEmpty() || username == null){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("Invalid username %s", username));
        }
        if (password.trim().isEmpty() || password == null){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("Invalid password"));
        }
        Account account = accountRepository.findByEmail(email);
        if (account != null){
            throw new GameShopException(HttpStatus.NOT_FOUND,
					String.format("Account with email %s already exists.", email));
        }

        Manager manager = new Manager(email, username, password, phoneNumber, address);
        return managerRepo.save(manager);
    }

    @Transactional
    public Employee createEmployee(String email, String username, String password, String phoneNumber, String address){
        if (email.trim().isEmpty() || email == null){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
					String.format("Email is invalid"));
        }
        if (username.trim().isEmpty() || username == null){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("Invalid username %s", username));
        }
        if (password.trim().isEmpty() || password == null){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("Invalid password"));
        }
        Account account = accountRepository.findByEmail(email);
        if (account != null){
            throw new GameShopException(HttpStatus.NOT_FOUND,
					String.format("Account with email %s already exists.", email));
        }
        Employee employee = new Employee(email, username, password, phoneNumber, address);
        return employeeRepo.save(employee);
    }

    @Transactional
    public Customer createCustomer(String email, String username, String password, String phoneNumber, String address){
        if (email.trim().isEmpty() || email == null){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
					String.format("Email is invalid"));
        }
        if (username.trim().isEmpty() || username == null){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("Invalid username %s", username));
        }
        if (password.trim().isEmpty() || password == null){
            throw new GameShopException(HttpStatus.BAD_REQUEST,
            String.format("Invalid password"));
        }
        Account account = accountRepository.findByEmail(email);
        if (account != null){
            throw new GameShopException(HttpStatus.NOT_FOUND,
            String.format("Account with email %s already exists.", email));
        }
        
        Cart cart = new Cart();
        cartRepository.save(cart);

        Customer customer = new Customer(email, username, password, phoneNumber, address, cart);
        return customerRepo.save(customer);
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
    public Account updateAccount(String email, String username, String password, String phoneNumber, String address){
        Account account = getAccountByEmail(email);

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
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
			throw new GameShopException(HttpStatus.NOT_FOUND,
					String.format("There is no account with email %s.", email));
		}
		return account;
    }

    @Transactional
    public Employee getEmployeeAccountByEmail(String email){
        Employee account = employeeRepo.findByEmail(email);
        if (account == null) {
			throw new GameShopException(HttpStatus.NOT_FOUND,
					String.format("There is no employee account with email %s.", email));
		}
		return account;
    }

    @Transactional
    public Customer getCustomerAccountByEmail(String email){
        Customer account = customerRepo.findByEmail(email);
        if (account == null) {
			throw new GameShopException(HttpStatus.NOT_FOUND,
					String.format("There is no customer account with email %s.", email));
		}
		return account;
    }

    @Transactional
    public Employee archiveEmployee(String email){
        Employee employee = employeeRepo.findByEmail(email);
        if (employee != null){
            employee.setEmployeeStatus(EmployeeStatus.Archived);
            return employeeRepo.save(employee);
        }
        throw new GameShopException(HttpStatus.NOT_FOUND,
        String.format("Account with email %s does not exist.", email));
    }
}
