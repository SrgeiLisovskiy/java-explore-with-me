package ru.practicum.main.service;

import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getByIdCompilation(Long compId);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(UpdateCompilationDto updateCompilationDto, Long compId);
}
