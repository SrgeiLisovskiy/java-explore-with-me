package ru.practicum.main.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CommentDto;
import ru.practicum.main.dto.NewCommentDto;
import ru.practicum.main.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/users/{userId}/comments")
public class PrivateCommentController {
    private final CommentService commentService;
    @PostMapping("/events/{eventId}")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @Valid @RequestBody NewCommentDto newCommentDto){
        log.info("Выполнен запрос POST/users/{userId}/comments/events/{eventId}");
        return new ResponseEntity<>(commentService.createComment(userId,eventId, newCommentDto), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<CommentDto>> getCommentsByUser(@PathVariable Long userId){
        log.info("Выполнен запрос GET/users/{userId}/comments");
        return new ResponseEntity<>(commentService.getCommentsByUser(userId),HttpStatus.OK);
    }
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentsById(@PathVariable Long userId, @PathVariable Long commentId){
        log.info("Выполнен запрос GET/users/{userId}/comments/{commentId}");
        return new ResponseEntity<>(commentService.getCommentsById(userId, commentId), HttpStatus.OK);
    }
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentId){
        log.info("Выполнен запрос DEL//users/{userId}/comments/{commentId}");
        commentService.deleteComment(userId, commentId);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long userId, @PathVariable Long commentId,
                                                    @Valid @RequestBody NewCommentDto newCommentDto){
        log.info("Выполнен запрос PATCH//users/{userId}/comments/{commentId}");
        return new ResponseEntity<>(commentService.updateComment(userId, commentId, newCommentDto), HttpStatus.OK);

    }
}
