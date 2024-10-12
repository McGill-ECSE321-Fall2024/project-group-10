package com.mcgill.ecse321.GameShop.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Employee;
import com.mcgill.ecse321.GameShop.model.Manager;


@SpringBootTest
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        categoryRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void testCreateAndReadCategory() {
       
        String email = "anthony.saber@hotmail.co";
        String username = "AnthonySaber";
        String password = "password";
        String phoneNumber = "+1 (438) 865-9291";
        String address = "1234 rue Sainte-Catherine";
    
    
    
        Manager createdManager = new Manager(email, username, password, phoneNumber, address);
        createdManager = accountRepository.save(createdManager);
    
        Category createdCategory = new Category("Electronics", createdManager);
        createdCategory = categoryRepository.save(createdCategory);
        int categoryId = createdCategory.getCategory_id();

        Category pulledCategory = categoryRepository.findById(categoryId);



        // Assertions
        assertNotNull(pulledCategory);
        assertNotNull(createdCategory);
        assertEquals("Electronics", pulledCategory.getCategoryName());
        assertEquals(createdManager.getEmail(), pulledCategory.getManager().getEmail());
        }

        
        
}








