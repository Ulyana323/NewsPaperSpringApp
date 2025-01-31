package ru.khav.NewsPaper.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "news")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class News {

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
    @ManyToMany(mappedBy = "news")
    private Set<Themes> themes = new HashSet<>();

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title +
                ", text='" + text ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof News)) return false;
        News news = (News) o;
        return id == news.id && title.equals(news.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title); // избегаем рекурсию
    }


}
