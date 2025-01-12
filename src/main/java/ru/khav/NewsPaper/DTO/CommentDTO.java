package ru.khav.NewsPaper.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommentDTO {
    @NotNull(message = "Full it, please")
    private String text;

}
