package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.NewCategoryDto;
import ru.practicum.main.exceptions.ConflictException;
import ru.practicum.main.exceptions.NotFoundException;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.mappers.CategoryMapper;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.utilite.CheckValidationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CheckValidationService checkValidation;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ConflictException("Категория с введенным именем уже существует");
        }
        Category category = categoryRepository.save(CategoryMapper.toCategory(categoryDto));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId) {
        Category category = checkExistence(catId);
        if (categoryRepository.findByNameAndIdNot(categoryDto.getName(), catId) != null) {
            throw new ConflictException("Нельзя изменить имя категории на уже занятое");
        }
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        List<Event> events = eventRepository.findByCategory(checkExistence(catId));
        if (!events.isEmpty()) {
            throw new ConflictException("Нельзя удалить категорию с привязанными событиями");
        }
        categoryRepository.deleteById(catId);

    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        PageRequest pageRequest = checkValidation.checkPageSize(from, size);
        List<Category> categories = categoryRepository.findAll(pageRequest).getContent();
        return categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        Category category = checkExistence(catId);
        return CategoryMapper.toCategoryDto(category);
    }

    private Category checkExistence(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Категория с ID = " + catId + "  не найдена"));
    }


}
