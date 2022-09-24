package ru.practicum.server.item.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentDto {
    Long id;
    String text;
    ItemDto item;
    String authorName;
    LocalDateTime created;
}
