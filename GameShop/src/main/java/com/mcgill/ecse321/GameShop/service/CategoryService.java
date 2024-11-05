package com.mcgill.ecse321.GameShop.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.repository.CategoryRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private GameRepository gameRepository;


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
        System.out.println("XIXI"+category.getCategoryName());
        return category;
    }
    
    @Transactional
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    @Transactional
    public Iterable<Game> getAllGamesInCategory(int categoryId) {
        ArrayList<Game> games = new ArrayList<Game>();
        for (Game game : gameRepository.findAll()) {
            if (game.getCategory(categoryId).getCategory_id() == categoryId){
            games.add(game);
        }

        
    }
    return games;
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
        category.removeManager();
        categoryRepository.save(category);
        categoryRepository.delete(category);
    }


}
