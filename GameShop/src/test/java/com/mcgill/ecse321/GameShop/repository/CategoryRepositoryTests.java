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

import jakarta.transaction.Transactional;

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
    @Transactional
    public void testCreateAndReadCategory() {

        String email = "anthony1.saber@hotmail.co";
        String username = "AnthonySaber";
        String password = "password";
        String phoneNumber = "+1 (438) 865-9291";
        String address = "1234 rue Sainte-Catherine";

        Manager createdManager = new Manager(email, username, password, phoneNumber, address);
        createdManager = managerRepository.save(createdManager);

        Category firstCreatedCategory = new Category("thriller", createdManager);
        firstCreatedCategory = categoryRepository.save(firstCreatedCategory);
        int firstCategoryId = firstCreatedCategory.getCategory_id();

        Category firstPulledCategory = categoryRepository.findById(firstCategoryId);

        Category secondCreatedCategory = new Category("barbie", createdManager);
        secondCreatedCategory = categoryRepository.save(secondCreatedCategory);
        int secondCategoryId = secondCreatedCategory.getCategory_id();

        Category secondPulledCategory = categoryRepository.findById(secondCategoryId);
        // Assertions
        assertNotNull(firstPulledCategory);
        assertNotNull(secondCreatedCategory);
        assertEquals(firstCreatedCategory.getCategory_id(), firstPulledCategory.getCategory_id());
        assertEquals(secondCreatedCategory.getCategory_id(), secondPulledCategory.getCategory_id());

        assertEquals("thriller", firstPulledCategory.getCategoryName());
        assertEquals("barbie", secondPulledCategory.getCategoryName());

        assertEquals(createdManager.getEmail(), firstPulledCategory.getManager().getEmail());
        assertEquals(createdManager.getEmail(), secondPulledCategory.getManager().getEmail());
    }

}
