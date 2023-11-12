package ru.practicum.main.service;

import ru.practicum.main.dto.CommentDto;
import ru.practicum.main.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getCommentsByUser(Long userId);

    CommentDto getCommentsById(Long userId, Long commentId);

    void deleteComment(Long userId, Long commentId);

    CommentDto updateComment(Long userId, Long commentId, NewCommentDto newCommentDto);

    CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    List<CommentDto> searchComments(String text, Integer from, Integer size);

    void deleteCommentByIdAdmin(Long commentId);

    List<CommentDto> getCommentsEventById(Long eventId, Integer from, Integer size);
}
