package ru.khav.NewsPaper.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class NewsShowDTO {

    private String title;

    private String text;

    private List<CommentShowDTO> Comments;

    private boolean isLikedByThisUser;

    private int countLikes;

    private LocalDateTime createdAt;

}