package ru.practicum.stats.server.service;

import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    HitDto addHit(HitDto hitDto);

    List<StatDto> getAllStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

    Long getView(Long eventId);
}
