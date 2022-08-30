package ru.practicum.shareit.item.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentDto {
    Long id;
    String text;
    Item item;
    String authorName;
    LocalDateTime created;
}
