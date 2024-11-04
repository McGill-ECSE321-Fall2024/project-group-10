package com.mcgill.ecse321.GameShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategoryListDto;
import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategoryRequestDto;
import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategoryResponseDto;
import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategorySummaryDto;
import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.service.CategoryService;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /** Create a new category */
    @PostMapping("/categories")
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryRequestDto request) {
        Category category = categoryService.createCategory(request.getCategoryName(), request.getManagerEmail());
        return CategoryResponseDto.create(category);
    }

    /** Get a category by ID */
    @GetMapping("/categories/{cid}")
    public CategoryResponseDto getCategoryById(@PathVariable int cid) {
        Category category = categoryService.getCategory(cid);
        return CategoryResponseDto.create(category);
    }

    /** Get all categories */
    @GetMapping("/categories")
    public CategoryListDto getAllCategories() {
        List<CategorySummaryDto> dtos = new ArrayList<CategorySummaryDto>();
        for (Category category : categoryService.getAllCategories()) {
            dtos.add(new CategorySummaryDto(category));
        }
        return new CategoryListDto(dtos);
    }

    /** Update a category's name */
    @PutMapping("/categories/{id}")
    public CategoryResponseDto updateCategory(@PathVariable int id, @RequestBody CategoryRequestDto request) {
        Category category = categoryService.updateCategory(id, request.getCategoryName());
        return CategoryResponseDto.create(category);
    }

    /** Delete a category */
    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
    }
}
