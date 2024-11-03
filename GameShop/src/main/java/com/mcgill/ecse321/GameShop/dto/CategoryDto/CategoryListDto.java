package com.mcgill.ecse321.GameShop.dto.CategoryDto;

import java.util.List;

public class CategoryListDto {
    private List<CategorySummaryDto> categories;

    public CategoryListDto(List<CategorySummaryDto> categories) {
        this.categories = categories;
    }

    public List<CategorySummaryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategorySummaryDto> categories) {
        this.categories = categories;
    }
}
