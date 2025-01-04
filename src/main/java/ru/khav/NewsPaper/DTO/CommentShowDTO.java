package ru.khav.NewsPaper.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor

public class CommentShowDTO {
        public CommentShowDTO() {
        }

        private String text;

        private String OwnerName;

        private String OwnerLastName;

        private Date CreatedAt;

    }

