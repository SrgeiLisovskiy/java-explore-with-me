package ru.practicum.main.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.EventDto;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/events")
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsByPublic(
            @RequestParam(required = false, name = "text") String text,
            @RequestParam(required = false, name = "categories") List<Long> categories,
            @RequestParam(required = false, name = "paid") Boolean paid,
            @RequestParam(required = false, name = "rangeStart") String rangeStart,
            @RequestParam(required = false, name = "rangeEnd") String rangeEnd,
            @RequestParam(required = false, defaultValue = "false", name = "onlyAvailable") Boolean onlyAvailable,
            @RequestParam(required = false, name = "sort") String sort,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
            HttpServletRequest request) {

        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        log.info("Выполнен запрос GET/events");
        return new ResponseEntity<>(eventService.getEventsByPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, uri, ip), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id, HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        log.info("Выполнен запрос GET/events/{id} :{}", id);
        return new ResponseEntity<>(eventService.getEventById(id, uri, ip), HttpStatus.OK);
    }
}
