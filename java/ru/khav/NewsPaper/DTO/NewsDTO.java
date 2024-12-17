package ru.khav.NewsPaper.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
@AllArgsConstructor
@Data
public class NewsDTO {

    @NotNull(message = "Full it, please")
    private String title;


    @NotNull(message = "Full it, please")
    private String text;

}
