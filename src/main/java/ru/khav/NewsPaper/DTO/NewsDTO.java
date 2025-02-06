package ru.khav.NewsPaper.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@AllArgsConstructor
@Data
public class NewsDTO {

    @NotNull
    private int id;

    @NotNull(message = "Full it, please")
    private String title;


    @NotNull(message = "Full it, please")
    private String text;

    private String imgSource;


    private Set<String> themes;

}
