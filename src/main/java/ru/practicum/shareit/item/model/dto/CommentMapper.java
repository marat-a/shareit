package ru.practicum.shareit.item.model.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static Comment toComment(NewCommentDTO newCommentDto, User author_id, Item item) {
        Comment comment = new Comment();
        comment.setAuthor(author_id);
        comment.setCreated(LocalDateTime.now());
        comment.setText(newCommentDto.getText());
        comment.setItem(item);
        return comment;
    }

    public static List<CommentDto> toCommentDtoList(List<Comment> commentList) {
        return commentList.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
