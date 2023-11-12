package ru.practicum.main.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CommentDto;
import ru.practicum.main.service.CommentService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class AdminCommentController {
    private final CommentService commentService;

    @GetMapping("/search")
    public ResponseEntity<List<CommentDto>> searchComments(@RequestParam(name = "text") String text,
                                                           @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Выполнен запрос GET/admin/comments/search");
        return new ResponseEntity<>(commentService.searchComments(text, from, size), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        log.info("Выполнен запрос DEL/admin/comments/{commentId}");
        commentService.deleteCommentByIdAdmin(commentId);
    }
}