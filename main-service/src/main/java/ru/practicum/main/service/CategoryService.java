package ru.practicum.main.service;

import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);

    void deleteCategory(Long catId);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long categoryId);

}
