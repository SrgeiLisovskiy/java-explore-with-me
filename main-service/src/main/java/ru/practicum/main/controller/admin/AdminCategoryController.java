package ru.practicum.main.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.NewCategoryDto;
import ru.practicum.main.service.CategoryService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> addNewCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Выполнен запрос POST/admin/categories: {}", newCategoryDto);
        return new ResponseEntity<>(categoryService.createCategory(newCategoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Выполнен запрос DEL/admin/categories/{catId} : {}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryDto categoryDto,
                                                      @PathVariable Long catId) {
        log.info("Выполнен запрос PATH/admin/categories/{catId}: {}", catId);
        return new ResponseEntity<>(categoryService.updateCategory(categoryDto, catId), HttpStatus.OK);
    }
}
