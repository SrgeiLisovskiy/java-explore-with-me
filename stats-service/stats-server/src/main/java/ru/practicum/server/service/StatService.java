package ru.practicum.server.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    HitDto addHit(HitDto hitDto);

    List<StatDto> getAllStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
