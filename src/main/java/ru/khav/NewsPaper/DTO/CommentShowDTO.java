package ru.khav.NewsPaper.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class CommentShowDTO {
    public CommentShowDTO() {
    }
    private int id;

    private String text;

    private String OwnerName;

    private String OwnerLastName;

    private Date CreatedAt;

}

