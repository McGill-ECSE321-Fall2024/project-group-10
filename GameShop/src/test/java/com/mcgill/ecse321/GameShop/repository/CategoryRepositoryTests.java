package com.mcgill.ecse321.GameShop.repository;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.model.Manager;


@SpringBootTest
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ManagerRepository managerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        categoryRepository.deleteAll();
        managerRepository.deleteAll();
    }

    @Test
    public void testCreateAndReadCategory() {
       
        String email = "anthony1.saber@hotmail.co";
        String username = "AnthonySaber";
        String password = "password";
        String phoneNumber = "+1 (438) 865-9291";
        String address = "1234 rue Sainte-Catherine";
    
    
    
        Manager createdManager = new Manager(email, username, password, phoneNumber, address);
        createdManager = managerRepository.save(createdManager);
    
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








