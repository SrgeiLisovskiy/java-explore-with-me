package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatClient;
import ru.practicum.dto.Create;
import ru.practicum.dto.HitDto;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.server.utilite.Constant.DATE_FORMAT;

@RestController
@AllArgsConstructor
@Slf4j
public class StateController {
    private final StatClient statClient;

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

