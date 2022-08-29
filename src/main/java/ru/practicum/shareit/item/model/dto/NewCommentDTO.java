package ru.practicum.shareit.item.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewCommentDTO {
    @NotBlank
    private String text;
}
