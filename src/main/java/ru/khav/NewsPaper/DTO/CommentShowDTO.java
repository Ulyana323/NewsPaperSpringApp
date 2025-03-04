package ru.khav.NewsPaper.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentShowDTO {

    private int id;

    private String text;

    private String OwnerName;

    private String OwnerLastName;

    private Date CreatedAt;


}

