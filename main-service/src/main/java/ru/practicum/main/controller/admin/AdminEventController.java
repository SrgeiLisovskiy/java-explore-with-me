package ru.practicum.main.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.EventDto;
import ru.practicum.main.dto.UpdateEventDto;
import ru.practicum.main.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventDto>> getEventsByAdmin(
            @RequestParam(required = false, name = "users") List<Long> users,
            @RequestParam(required = false, name = "states") List<String> states,
            @RequestParam(required = false, name = "categories") List<Long> categories,
            @RequestParam(required = false, name = "rangeStart") String rangeStart,
            @RequestParam(required = false, name = "rangeEnd") String rangeEnd,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Выполнен запрос GET/admin/events");
        return new ResponseEntity<>(eventService.getEventsByAdmin(users, states, categories, rangeStart,
                rangeEnd, from, size), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEventByAdmin(@Validated @RequestBody UpdateEventDto updateEventDto,
                                                       @PathVariable Long eventId) {
        log.info("Выполнен запрос PATH/admin/events/{eventId} где updateEventDto= {}, eventId= {}",
                updateEventDto, eventId);
        return new ResponseEntity<>(eventService.updateEventByAdmin(updateEventDto, eventId), HttpStatus.OK);
    }


}
