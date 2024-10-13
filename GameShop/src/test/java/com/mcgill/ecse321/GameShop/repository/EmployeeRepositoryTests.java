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
import com.mcgill.ecse321.GameShop.model.Employee;

import jakarta.transaction.Transactional;

@SpringBootTest
public class EmployeeRepositoryTests {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        employeeRepository.deleteAll();
        cartRepository.deleteAll();
       
    }

    @Test
    @Transactional
        public void testCreateAndReadEmployeeAccountAsAccount() {
            // create customer1
            String email = "anthony.saber.02@hotmail.com";
            String username = "AnthonySaber";
            String password = "password";
            String phoneNumber = "+1 (438) 865-9295";
            String address = "1234 rue Sainte-Catherine";



            Employee createdEmployee = new Employee(email, username, password, phoneNumber, address);
            createdEmployee = employeeRepository.save(createdEmployee);
            
            Account pulledEmployee = employeeRepository.findByEmail(email);

            assertNotNull(createdEmployee);
            assertEquals(email, pulledEmployee.getEmail());
            assertEquals(username, pulledEmployee.getUsername());
            assertEquals(password, pulledEmployee.getPassword());
            assertEquals(phoneNumber, pulledEmployee.getPhoneNumber());
            assertEquals(address, pulledEmployee.getAddress());
            assertTrue(pulledEmployee instanceof Employee, "The account should be an employee.");
            
        }
}
