package ru.khav.NewsPaper.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "news")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class News {

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                ", comments=" + comments.size() +
                ", likes=" + likes.size() +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    @NonNull
    private String title;

    @Column(name = "text")
    @NonNull
    private String text;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_up")
    @UpdateTimestamp
    private LocalDateTime updatedUp;

    @OneToMany(mappedBy = "news", fetch = FetchType.EAGER)
    private List<Comment> comments;


    @OneToMany(mappedBy = "newsOwnLike", fetch = FetchType.EAGER)
    private List<Like> likes;




}
