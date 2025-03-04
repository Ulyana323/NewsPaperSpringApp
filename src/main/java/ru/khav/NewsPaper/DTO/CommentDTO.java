package ru.khav.NewsPaper.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    @NotNull(message = "Full it, please")
    private String text;

}
