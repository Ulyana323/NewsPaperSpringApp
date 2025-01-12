package ru.khav.NewsPaper.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "news")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    @NotNull(message = "Full it, please")
    private String title;

    @Column(name = "text")
    @NotNull(message = "Full it, please")
    private String text;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "isliked")
    private boolean isLiked;

    @OneToMany(mappedBy = "news", fetch = FetchType.EAGER)
    private List<Comment> comments;

    //todo количество лайков


}
