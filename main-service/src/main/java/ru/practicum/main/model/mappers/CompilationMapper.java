package ru.practicum.main.model.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation) {
        Set<EventShortDto> events = compilation.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toSet());
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(events)
                .build();
    }

    public static Compilation toCompilation(NewCompilationDto compilationDto, Set<Event> events) {
        return Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned())
                .events(events)
                .build();
    }
}
