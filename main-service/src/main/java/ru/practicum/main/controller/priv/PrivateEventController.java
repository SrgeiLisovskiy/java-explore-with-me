package ru.practicum.main.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.*;
import ru.practicum.main.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getAllEventsByUserId(@PathVariable(value = "userId") Long userId,
                                                    @RequestParam(value = "from", defaultValue = "0")
                                                    @PositiveOrZero Integer from,
                                                    @RequestParam(value = "size", defaultValue = "10")
                                                    @Positive Integer size) {
        log.info("GET запрос на получения событий пользователя с id= {}", userId);
        return eventService.getEventsByUserId(userId, from, size);
    }


    @PostMapping
    public ResponseEntity<EventDto> createEvent(@RequestBody @Validated NewEventDto newEventDto,
                                                @PathVariable Long userId) {
        log.info("Выполнен запрос POST/users/{userId}/events , userId= {}, EventDto= {}", userId, newEventDto);
        return new ResponseEntity<>(eventService.createEvent(newEventDto, userId), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getUserEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Выполнен запрос GET/users/{userId}/events/{eventId} , userId= {}, eventId= {}", userId, eventId);
        return new ResponseEntity<>(eventService.getUserEventById(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventDto> updateUserEventById(@RequestBody @Validated UpdateEventDto updateEventDto,
                                                        @PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Выполнен запрос PATH/users/{userId}/events/{eventId}, updateEventDto= {}, userId= {}, eventId= {}",
                updateEventDto, userId, eventId);
        return new ResponseEntity<>(eventService.updateUserEventById(updateEventDto, userId, eventId), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getRequestsForEventIdByUserId(@PathVariable Long userId,
                                                                          @PathVariable Long eventId) {
        log.info("Выполнен запрос GET/users/{userId}/events/{eventId}/requests, где userId= {}, eventId= {}", userId,
                eventId);
        return new ResponseEntity<>(eventService.getRequestsForEventIdByUserId(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<RequestUpdateDtoResult> updateStatusRequestsForEventIdByUserId(
            @Validated @RequestBody RequestUpdateDtoRequest requestUpdateDtoRequest,
            @PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Выполнен запрос PATH/users/{userId}/events/{eventId}/requests где: " +
                "userId= {}, eventId= {}, requestDto= {}", userId, eventId, requestUpdateDtoRequest);
        return new ResponseEntity<>(eventService.updateStatusRequestsForEventIdByUserId(requestUpdateDtoRequest,
                userId, eventId), HttpStatus.OK);
    }
}
