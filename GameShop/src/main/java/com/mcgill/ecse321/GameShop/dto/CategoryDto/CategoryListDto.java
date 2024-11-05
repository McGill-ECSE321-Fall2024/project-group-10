package com.mcgill.ecse321.GameShop.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;

import com.mcgill.ecse321.GameShop.model.Category;

public class CategoryListDto {
    private List<CategorySummaryDto> categories;

    public CategoryListDto(List<CategorySummaryDto> categories) {
        this.categories = categories;
    }

    public CategoryListDto() {
    }

    public List<CategorySummaryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategorySummaryDto> categories) {
        this.categories = categories;
    }

    public CategoryListDto convertToCategoryListDto(List<Category> categories) {
        List<CategorySummaryDto> categorySummaryDtos = new ArrayList<>();
        for (Category category : categories) {
            categorySummaryDtos.add(new CategorySummaryDto(category));
        }
        return new CategoryListDto(categorySummaryDtos);

    }
}
