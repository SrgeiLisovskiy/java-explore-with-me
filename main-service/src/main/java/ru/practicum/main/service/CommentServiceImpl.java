package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.CommentDto;
import ru.practicum.main.dto.NewCommentDto;
import ru.practicum.main.exceptions.ConflictException;
import ru.practicum.main.exceptions.NotFoundException;
import ru.practicum.main.model.Comment;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.User;
import ru.practicum.main.model.enums.EventState;
import ru.practicum.main.model.mappers.CommentMapper;
import ru.practicum.main.repository.CommentRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.utilite.CheckValidationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final CheckValidationService checkValidationService;

    @Override
    public List<CommentDto> getCommentsByUser(Long userId) {
        checkExistenceUser(userId);
        return commentRepository.findByAuthor_Id(userId).stream()
                .map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentsById(Long userId, Long commentId) {
        checkExistenceUser(userId);
        checkExistenceComment(commentId);
        Comment comment = checkExistenceCommentAuthor(userId, commentId);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        checkExistenceUser(userId);
        checkExistenceComment(commentId);
        checkExistenceCommentAuthor(userId, commentId);
        log.info("Коментарий с ID= " + commentId + " удален");
        commentRepository.deleteById(commentId);

    }

    @Override
    @Transactional
    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto newCommentDto) {
        checkExistenceUser(userId);
        checkExistenceComment(commentId);
        Comment comment = checkExistenceCommentAuthor(userId, commentId);
        comment.setText(newCommentDto.getText());
        comment.setLastUpdated(LocalDateTime.now());
        log.info("Коментарий обнавлен: {}", comment);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = checkExistenceUser(userId);
        Event event = checkExistenceEvent(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Невозможно добавить комментарий к событию со статусом не PUBLISHED");
        }
        Comment comment = CommentMapper.toComment(newCommentDto, user, event);
        Comment commentResult = commentRepository.save(comment);
        log.info("Коментарий сохранен: {}", comment);
        return CommentMapper.toCommentDto(commentResult);
    }

    @Override
    public List<CommentDto> searchComments(String text, Integer from, Integer size) {
        PageRequest pageRequest = checkValidationService.checkPageSize(from, size);
        Specification<Comment> specification = Specification.where(null);
        if (text != null) {
            String searchText = text.toLowerCase();
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("text")), "%" + searchText + "%")
                    ));
        }
        List<Comment> events = commentRepository.findAll(specification, pageRequest).getContent();
        return events.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCommentByIdAdmin(Long commentId) {
        checkExistenceComment(commentId);
        log.info("Коментарий с ID= " + commentId + " удален");
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getCommentsEventById(Long eventId, Integer from, Integer size) {
        checkExistenceEvent(eventId);
        PageRequest pageRequest = checkValidationService.checkPageSize(from, size);
        log.info("Выведен список коментариев события с ID= {}", eventId);
        return commentRepository.findByEvent_Id(eventId, pageRequest).stream()
                .map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    private User checkExistenceUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с ID = " + userId + "  не найден"));
    }

    private Event checkExistenceEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("События с ID = " + eventId + "  не найдено"));
    }

    private Comment checkExistenceCommentAuthor(Long userId, Long commentId) {
        return commentRepository.findByAuthor_IdAndId(userId, commentId).orElseThrow(
                () -> new NotFoundException("Коментария с ID = " + commentId + "и автором с ID = " + userId +
                        " не существует"));
    }

    private Comment checkExistenceComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Коментарий с ID= " + commentId + " не найден"));
    }
}
