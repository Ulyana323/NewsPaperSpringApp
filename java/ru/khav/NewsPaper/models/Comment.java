package ru.khav.NewsPaper.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="created_at")
    private Date createdAt;

    @Column(name="comment_text")
    private String text;

    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "id")
    private Person owner;

    @ManyToMany(mappedBy = "comments",fetch = FetchType.EAGER)
    private List<News> CommentNews;

}
