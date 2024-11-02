package com.mcgill.ecse321.GameShop.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Platform;
import com.mcgill.ecse321.GameShop.repository.CategoryRepository;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;
import com.mcgill.ecse321.GameShop.repository.PlatformRepository;

import jakarta.transaction.Transactional;

@Service
public class PlatformAndCategoryService {
    @Autowired
    private PlatformRepository platformRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ManagerRepository managerRepository;

    @Transactional
    public Platform createPlatform(String platformName, String managerEmail) {
        if (platformName == null || platformName.trim().isEmpty()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Platform name cannot be empty or null");
        }

        Manager manager = managerRepository.findByEmail(managerEmail);
        if (manager == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("There is no manager with email:", managerEmail));
        }
        Platform platform = new Platform(platformName, manager);
        return platformRepository.save(platform);

    }

    @Transactional
    public Platform getPlatform(int platformId) {
        Platform platform = platformRepository.findById(platformId);
        if (platform == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("Platform does not exist"));
        }
        return platform;
    }

    @Transactional
    public Iterable<Platform> getAllPlatforms() {
        return platformRepository.findAll();
    }

    @Transactional
    public Platform updatePlatform(int platformId, String platformName) {
        Platform platform = getPlatform(platformId);
        if (platformName == null || platformName.trim().isEmpty()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Platform name cannot be empty or null");
        }
        platform.setPlatformName(platformName);
        return platformRepository.save(platform);
    }

    @Transactional
    public void deletePlatform(int platformId) {
        Platform platform = getPlatform(platformId);
        platformRepository.delete(platform);
    }



    @Transactional
    public Category createCategory(String categoryName, String managerEmail) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Category name cannot be empty or null");
        }
    
        Manager manager = managerRepository.findByEmail(managerEmail);
        if (manager == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("There is no manager with email:", managerEmail));
        }
        Category category = new Category(categoryName, manager);
        return categoryRepository.save(category);
    }
    
    @Transactional
    public Category getCategory(int categoryId) {
        Category category = categoryRepository.findById(categoryId);
        if (category == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("Category does not exist"));
        }
        return category;
    }
    
    @Transactional
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    @Transactional
    public Category updateCategory(int categoryId, String categoryName) {
        Category category = getCategory(categoryId);
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Category name cannot be empty or null");
        }
        category.setCategoryName(categoryName);
        return categoryRepository.save(category);
    }
    
    @Transactional
    public void deleteCategory(int categoryId) {
        Category category = getCategory(categoryId);
        categoryRepository.delete(category);
    }


}
