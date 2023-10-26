package ru.practicum.server.model.mapper;

import ru.practicum.dto.HitDto;
import ru.practicum.server.model.Stat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.server.utilite.Constant.DATE_FORMAT;

public class StatMapper {
    public static HitDto toHitDto(Stat stat) {
        return HitDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .timestamp(stat.getTimestamp().toString())
                .build();
    }

    public static Stat toStat(HitDto hitDto) {
        return Stat.builder()
                .ip(hitDto.getIp())
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .timestamp(LocalDateTime.parse(hitDto.getTimestamp(),
                        DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .build();
    }
}
