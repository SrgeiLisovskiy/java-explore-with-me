package ru.practicum.stats.stats_client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.Create;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.stats_client.client.StatClient;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StateController {
    private final StatClient statClient;

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    public ResponseEntity<Object> saveHit(@Validated({Create.class}) @RequestBody HitDto hitDto) {
        log.debug("Получен запрос: POST /hit , где HitDto = " + hitDto);
        return statClient.addHit(hitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getAllStats(@RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
                                              @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
                                              @RequestParam(required = false, name = "uris") List<String> uris,
                                              @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.debug("Получен запрос: GET/stats , где start=" + start + " end time=" + end + " uris=" + uris +
                " unique=" + unique);
        return statClient.getAllStats(start, end, uris, unique);
    }
}

