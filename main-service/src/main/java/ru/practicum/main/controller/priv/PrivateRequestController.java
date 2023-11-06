package ru.practicum.main.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.RequestDto;
import ru.practicum.main.service.RequestService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateRequestController {
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<RequestDto>> getRequestsByUserId(@PathVariable Long userId) {
        log.info("Выполнен запрос GET/users/{userId}/requests, где userId= {}", userId);
        return new ResponseEntity<>(requestService.getRequestsByUserId(userId), HttpStatus.OK);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<RequestDto> createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Выполнен запрос POST/users/{userId}/requests");
        return new ResponseEntity<>(requestService.createRequest(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    @Transactional
    public ResponseEntity<RequestDto> cancelRequest(@PathVariable Long userId,
                                                    @PathVariable Long requestId) {

        log.info("Выполнен запрос PATCH/users/{userId}/requests/{requestId}/cancel, где userId= {}, requestId= {}",
                userId, requestId);
        return new ResponseEntity<>(requestService.cancelRequest(userId, requestId), HttpStatus.OK);
    }
}
