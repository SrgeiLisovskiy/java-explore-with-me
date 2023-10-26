package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;
import ru.practicum.server.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.server.utilite.Constant.DATE_FORMAT;

@RequiredArgsConstructor
@RestController
public class StatServiceController {
    private final StatService statService;

    @PostMapping("/hit")
    public HitDto addHit(@RequestBody HitDto hitDto) {
        return statService.addHit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> getAllStats(@RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
                                     @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
                                     @RequestParam(required = false) List<String> uris,
                                     @RequestParam(defaultValue = "false") boolean unique) {
        return statService.getAllStats(start, end, uris, unique);
    }
}
