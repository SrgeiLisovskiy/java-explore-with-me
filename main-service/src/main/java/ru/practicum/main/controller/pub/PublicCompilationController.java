package ru.practicum.main.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/compilations")
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getAllCompilations(
            @RequestParam(defaultValue = "false") Boolean pinned,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Выполнен запрос GET/compilations, pinned={}, from={}, size={}", pinned, from, size);
        return new ResponseEntity<>(compilationService.getAllCompilations(pinned, from, size), HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable Long compId) {
        log.info("GET/compilations/{compId}: {}", compId);
        return new ResponseEntity<>(compilationService.getByIdCompilation(compId), HttpStatus.OK);
    }
}
