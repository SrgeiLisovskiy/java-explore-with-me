package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.dto.UpdateCompilationDto;
import ru.practicum.main.exceptions.NotFoundException;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.mappers.CompilationMapper;
import ru.practicum.main.repository.CompilationRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.utilite.CheckValidationService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CheckValidationService checkValidation;

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = checkValidation.checkPageSize(from, size);
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(pageRequest).getContent();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, pageRequest).getContent();
        }
        return compilations.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Collection<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto,
                new HashSet<>(events)));
        return CompilationMapper.toCompilationDto(compilation);
    }


    @Override
    public CompilationDto getByIdCompilation(Long compId) {
        Compilation compilation = checkExistence(compId);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        checkExistence(compId);
        compilationRepository.deleteById(compId);
    }


    @Override
    @Transactional
    public CompilationDto updateCompilation(UpdateCompilationDto compilationDto, Long compId) {
        Compilation compilation = checkExistence(compId);
        if (compilationDto.getTitle() != null) {
            compilation.setTitle(compilationDto.getTitle());
        }

        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
        }

        if (compilationDto.getEvents() != null) {
            compilation.setEvents(new HashSet<>(eventRepository.findAllById(compilationDto.getEvents())));
        }

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    private Compilation checkExistence(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Сборка с ID = " + compId + "  не найдена"));
    }
}
