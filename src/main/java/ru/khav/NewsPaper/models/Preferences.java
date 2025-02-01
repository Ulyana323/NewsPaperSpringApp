package ru.khav.NewsPaper.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "preferences")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Preferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @Column(name = "user_id")
    private int userId;

    @NonNull
    @Column(name = "theme_id")
    private int themeId;

    @NonNull
    @Column(name = "prefered_status")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Person user;

    @ManyToOne
    @JoinColumn(name = "theme_id", referencedColumnName = "id")
    private Themes theme;
}
