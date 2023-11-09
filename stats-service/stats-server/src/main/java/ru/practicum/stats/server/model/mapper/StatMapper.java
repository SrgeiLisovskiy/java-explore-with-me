package ru.practicum.stats.server.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.server.model.Stat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.stats.server.utilite.Constant.DATE_FORMAT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatMapper {

    public static Stat toStat(HitDto hitDto) {
        LocalDateTime time = LocalDateTime.parse(hitDto.getTimestamp(), DateTimeFormatter.ofPattern(DATE_FORMAT));
        return Stat.builder()
                .ip(hitDto.getIp())
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .timestamp(time)
                .build();
    }
}
