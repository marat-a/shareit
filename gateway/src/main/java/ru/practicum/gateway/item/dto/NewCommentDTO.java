package ru.practicum.gateway.item.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class NewCommentDTO {
    @NotBlank
    private String text;
}
