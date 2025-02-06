package ru.khav.NewsPaper.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "likes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    private News newsOwnLike;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Person personOwnLike;

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", newsOwnLike=" + newsOwnLike.getId() +
                ", personOwnLike=" + personOwnLike.getName() +
                '}';
    }


}
