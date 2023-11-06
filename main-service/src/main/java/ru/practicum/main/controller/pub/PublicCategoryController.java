package ru.practicum.main.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/categories")
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Выполнен запрос GET/categories, from={}, size={}", from, size);
        return new ResponseEntity<>(categoryService.getCategories(from, size), HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long catId) {
        log.info("Выполнен запрос GET/categories/{catId}: {}", catId);
        return new ResponseEntity<>(categoryService.getCategoryById(catId), HttpStatus.OK);
    }


}
