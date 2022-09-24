package ru.practicum.server.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.item.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository  extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findAllByItemId(long itemId);
}
