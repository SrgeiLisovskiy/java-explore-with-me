package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.main.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    Optional<Comment> findByAuthor_IdAndId(Long userId, Long commentId);

    List<Comment> findByAuthor_Id(Long userId);

    Page<Comment> findByEvent_Id(Long eventId, Pageable pageable);

    Long countCommentByEvent_Id(Long eventId);
}
