package ru.khav.NewsPaper.DTO;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.Date;
@Data
public class CommentDTO {
    @NotNull(message = "Full it, please")
    private String text;



}
