package ru.practicum.stats.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatDto;
import ru.practicum.stats.server.service.StatService;
import ru.practicum.stats.server.utilite.Constant;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StatServiceController {
    private final StatService statService;


    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")

    public ResponseEntity<HitDto> addHit(@RequestBody HitDto hitDto) {
        return new ResponseEntity<>(statService.addHit(hitDto), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public List<StatDto> getAllStats(@RequestParam @DateTimeFormat(pattern = Constant.DATE_FORMAT) LocalDateTime start,
                                     @RequestParam @DateTimeFormat(pattern = Constant.DATE_FORMAT) LocalDateTime end,
                                     @RequestParam(required = false) List<String> uris,
                                     @RequestParam(defaultValue = "false") boolean unique) {
        return statService.getAllStats(start, end, uris, unique);
    }
}
