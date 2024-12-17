package ru.khav.NewsPaper.models;

import lombok.Data;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "news")
@Data
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
    private LocalDateTime created_at;

    @ManyToMany
    @JoinTable(name = "news_n_comments",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")

    )
    private List<Comment> comments;


}
