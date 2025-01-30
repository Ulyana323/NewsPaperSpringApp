package ru.khav.NewsPaper.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "likes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Like {
    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", newsOwnLike=" + newsOwnLike.getId() +
                ", personOwnLike=" + personOwnLike.getName() +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "news_id",referencedColumnName = "id")
    private News newsOwnLike;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private Person personOwnLike;



}
