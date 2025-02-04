package ru.khav.NewsPaper.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.khav.NewsPaper.models.Themes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
public class NewsShowDTO {

    private String title;

    private String text;

    private List<CommentShowDTO> Comments;

    private boolean isLikedByThisUser;

    private int countLikes;

    private LocalDateTime createdAt;

   private String imgSource;

    private Set<String> themes;

}