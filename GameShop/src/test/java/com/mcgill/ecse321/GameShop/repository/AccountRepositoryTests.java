package com.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void testCreateAndReadEmployeeAccount() {
        // Create Employee 1
        String email = "2@2.com";
        String username = "AnthonySaber";
        String password = "password";
        String phoneNumber = "+1 (438) 865-9295";
        String address = "1234 rue Sainte-Catherine";

        Employee createdEmployee = new Employee(email, username, password, phoneNumber, address);
        createdEmployee = accountRepository.save(createdEmployee);

        Account pulledEmployee = accountRepository.findByEmail(email);

        assertNotNull(createdEmployee);
        assertEquals(email, pulledEmployee.getEmail());
        assertEquals(username, pulledEmployee.getUsername());
        assertEquals(password, pulledEmployee.getPassword());
        assertEquals(phoneNumber, pulledEmployee.getPhoneNumber());
        assertEquals(address, pulledEmployee.getAddress());
        assertTrue(pulledEmployee instanceof Employee, "The account should be an employee.");

    }

    @Test
    public void testCreateAndReadManagerAccount() {
        // Create Manager
        String email = "3@3.com";
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

    }

}
