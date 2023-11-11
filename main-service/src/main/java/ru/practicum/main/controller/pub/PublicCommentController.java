package ru.practicum.main.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CommentDto;
import ru.practicum.main.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/comments")
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping("/{eventId}")
    public ResponseEntity<List<CommentDto>> getCommentsEventById(@PathVariable Long eventId,
                                                                 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                                 @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Выполнен запрос GET/comments/{eventId}");
        return new ResponseEntity<>(commentService.getCommentsEventById(eventId, from, size), HttpStatus.OK);
    }
}
