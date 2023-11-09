package ru.practicum.main.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.dto.UpdateCompilationDto;
import ru.practicum.main.service.CompilationService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> creatCompilation(
            @Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Выполнен запрос POST/admin/compilations: {}", newCompilationDto);
        return new ResponseEntity<>(compilationService.createCompilation(newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Выполнен запрос DEL/admin/compilations/{compId}: {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(
            @Valid @RequestBody UpdateCompilationDto updateCompilationDto, @PathVariable Long compId) {
        log.info("Выполнен запрос PATH/admin/compilations/{compId}: {}", compId);
        return new ResponseEntity<>(compilationService.updateCompilation(updateCompilationDto, compId), HttpStatus.OK);
    }
}
