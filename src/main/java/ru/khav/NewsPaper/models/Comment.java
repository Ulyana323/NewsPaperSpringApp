package ru.khav.NewsPaper.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "comment_text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Person owner;

    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    private News news;

    public <T> Comment(int i, Date date, String ahahah, Person user, News news, List<Comment> ts) {
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", text='" + text + ", ownerId=" + (owner != null ? owner.getId() : "null") + //  ID владельца
                ", newsId=" + (news != null ? news.getId() : "null") + //  ID новости
                '}';
    }

}
