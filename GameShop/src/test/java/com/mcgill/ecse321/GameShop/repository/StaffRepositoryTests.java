package com.mcgill.ecse321.GameShop.repository;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Employee;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Staff;

import jakarta.transaction.Transactional;


@SpringBootTest
public class StaffRepositoryTests {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        staffRepository.deleteAll();
        cartRepository.deleteAll();
       
    }

        @Test
        @Transactional
        public void testCreateAndReadEmployeeAccountAsStaff() {
            // Create Employee 1
            String email = "anthony.saber@hotmail.commmmm";
            String username = "AnthonySaber";
            String password = "password";
            String phoneNumber = "+1 (438) 865-9295";
            String address = "1234 rue Sainte-Catherine";



            Employee createdEmployee = new Employee(email, username, password, phoneNumber, address);
            createdEmployee = staffRepository.save(createdEmployee);
            
            Staff pulledEmployee = staffRepository.findByEmail(email);

            assertNotNull(createdEmployee);
            assertEquals(email, pulledEmployee.getEmail());
            assertEquals(username, pulledEmployee.getUsername());
            assertEquals(password, pulledEmployee.getPassword());
            assertEquals(phoneNumber, pulledEmployee.getPhoneNumber());
            assertEquals(address, pulledEmployee.getAddress());
            assertTrue(pulledEmployee instanceof Employee, "The account should be an employee.");
            
        }

        @Test
        public void testCreateAndReadManagerAccountAsStaff() {
            // Create Manager
            String email = "anthony.saber@hotmail.commmmmmm";
            String username = "AnthonySaber";
            String password = "password";
            String phoneNumber = "+1 (438) 865-9293";
            String address = "1234 rue Sainte-Catherine";



            Manager createdManager = new Manager(email, username, password, phoneNumber, address);
            createdManager = staffRepository.save(createdManager);
            
            Staff pulledManager = staffRepository.findByEmail(email);

            assertNotNull(createdManager);
            assertEquals(email, pulledManager.getEmail());
            assertEquals(username, pulledManager.getUsername());
            assertEquals(password, pulledManager.getPassword());
            assertEquals(phoneNumber, pulledManager.getPhoneNumber());
            assertEquals(address, pulledManager.getAddress());
            assertTrue(pulledManager instanceof Manager, "The account should be a manager.");
            
        }

    }






