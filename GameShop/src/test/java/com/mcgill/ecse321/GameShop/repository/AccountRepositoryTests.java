package com.mcgill.ecse321.GameShop.repository;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Employee;
import com.mcgill.ecse321.GameShop.model.Manager;

import jakarta.transaction.Transactional;

@SpringBootTest
public class AccountRepositoryTests {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        accountRepository.deleteAll();
        cartRepository.deleteAll();

    }

    @Test
    @Transactional
    public void testCreateAndReadCustomerAccount() {
        // Create Customer 1
        String email = "1@1.com";
        String username = "AnthonySaber";
        String password = "password";
        String phoneNumber = "+1 (438) 865-9294";
        String address = "1234 rue Sainte-Catherine";

        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        int cartId = cart.getCart_id();

        Customer customer1 = new Customer(email, username, password, phoneNumber, address, cart);
        customer1 = accountRepository.save(customer1);

        Account account2 = accountRepository.findByEmail(email);

        assertNotNull(customer1);
        assertEquals(email, account2.getEmail());
        assertEquals(username, account2.getUsername());
        assertEquals(password, account2.getPassword());
        assertEquals(phoneNumber, account2.getPhoneNumber());
        assertEquals(address, account2.getAddress());
        assertTrue(account2 instanceof Customer, "The account should be a customer.");
        assertEquals(cartId, ((Customer) account2).getCart().getCart_id());

    }

    @Test
    @Transactional
    public void testCreateAndReadCustomerWithCartReplacement() {
        // create firstCustomer
        String email = "8@8.com";
        String username = "AnthonySaber";
        String password = "password";
        String phoneNumber = "+1 (438) 865-9294";
        String address = "1234 rue Sainte-Catherine";

        Cart firstCart = new Cart();
        firstCart = cartRepository.save(firstCart);
        int firstCardId = firstCart.getCart_id();

        Customer firstCustomer = new Customer(email, username, password, phoneNumber, address, firstCart);
        firstCustomer = accountRepository.save(firstCustomer);

        Account pulledFirstCustomer = accountRepository.findByEmail(email);

        Cart secondCart = new Cart();
        secondCart = cartRepository.save(secondCart);
        int secondCartId = secondCart.getCart_id();

        firstCustomer.setCart(secondCart);

        assertNotNull(firstCustomer);
        assertEquals(email, pulledFirstCustomer.getEmail());
        assertEquals(username, pulledFirstCustomer.getUsername());
        assertEquals(password, pulledFirstCustomer.getPassword());
        assertEquals(phoneNumber, pulledFirstCustomer.getPhoneNumber());
        assertEquals(address, pulledFirstCustomer.getAddress());
        assertTrue(pulledFirstCustomer instanceof Customer, "The account should be a customer.");
        assertNotEquals(firstCardId, ((Customer) pulledFirstCustomer).getCart().getCart_id());
        assertEquals(secondCartId, ((Customer) pulledFirstCustomer).getCart().getCart_id());

    }

    @Test
    @Transactional
    public void testCreateAndReadEmployeeAccount() {
        // create employee 1
            String email = "9@9.com";
            String username = "AnthonySaber";
            String password = "password";
            String phoneNumber = "+1 (438) 865-9295";
            String address = "1234 rue Sainte-Catherine";



            Employee createdFirstEmployee = new Employee(email, username, password, phoneNumber, address);
            createdFirstEmployee = accountRepository.save(createdFirstEmployee);
            
            Account pulledFirstEmployee = accountRepository.findByEmail(email);

            String email2 = "10@10.com";
            String username2 = "AnthonySaber";
            String password2 = "password";
            String phoneNumber2 = "+1 (438) 865-9295";
            String address2 = "1234 rue Sainte-Catherine";



            Employee createdSecondEmployee = new Employee(email2, username2, password2, phoneNumber2, address2);
            createdSecondEmployee = accountRepository.save(createdSecondEmployee);
            
            Account pulledSecondEmployee = accountRepository.findByEmail(email2);

            assertNotNull(createdFirstEmployee);
            assertEquals(email, pulledFirstEmployee.getEmail());
            assertEquals(username, pulledFirstEmployee.getUsername());
            assertEquals(password, pulledFirstEmployee.getPassword());
            assertEquals(phoneNumber, pulledFirstEmployee.getPhoneNumber());
            assertEquals(address, pulledFirstEmployee.getAddress());
            assertTrue(pulledFirstEmployee instanceof Employee, "The account should be an employee.");

            assertNotNull(createdSecondEmployee);
            assertEquals(email2, pulledSecondEmployee.getEmail());
            assertEquals(username2, pulledSecondEmployee.getUsername());
            assertEquals(password2, pulledSecondEmployee.getPassword());
            assertEquals(phoneNumber2, pulledSecondEmployee.getPhoneNumber());
            assertEquals(address2, pulledSecondEmployee.getAddress());
            assertTrue(pulledSecondEmployee instanceof Employee, "The account should be an employee.");

            List<Account> employees = (List<Account>) accountRepository.findAll();
            assertEquals(2, employees.size());
            

    }

    @Test
    @Transactional
    public void testCreateAndReadManagerAccount() {
        // Create Manager
        String email = "anthony.saber.as@hotmail.commmmmm";
        String username = "AnthonySaber";
        String password = "password";
        String phoneNumber = "+1 (438) 865-9293";
        String address = "1234 rue Sainte-Catherine";

        Manager createdManager = new Manager(email, username, password, phoneNumber, address);
        createdManager = accountRepository.save(createdManager);
        
        Account pulledManager = accountRepository.findByEmail(email);

        assertNotNull(createdManager);
        assertEquals(email, pulledManager.getEmail());
        assertEquals(username, pulledManager.getUsername());
        assertEquals(password, pulledManager.getPassword());
        assertEquals(phoneNumber, pulledManager.getPhoneNumber());
        assertEquals(address, pulledManager.getAddress());
        assertTrue(pulledManager instanceof Manager, "The account should be a manager.");
        List<Account> managers = (List<Account>) accountRepository.findAll();

        // Assertions
        assertNotNull(managers);
        assertEquals(1, managers.size());
    }

}
