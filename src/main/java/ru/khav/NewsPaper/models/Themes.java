package ru.khav.NewsPaper.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "themes")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Themes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @Column(name = "theme_name")
    private String name;


    @ManyToMany
    @JoinTable(
            name = "themes_n_news",
            joinColumns = @JoinColumn(name = "theme_id"),
            inverseJoinColumns = @JoinColumn(name = "news_id"))
    private Set<News> news = new HashSet<>();


    @OneToMany(mappedBy = "theme", fetch = FetchType.EAGER)
    private Set<Preferences> preferences= new HashSet<>();

    public Themes(int i, String theme1) {
    }

    @Override
    public String toString() {
        return "Themes{" +
                "id=" + id +
                ", name='" + name ;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Themes)) return false;
        Themes themes = (Themes) o;
        return id == themes.id && name.equals(themes.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name); //чтобы не было рекурсии исключаем news
    }
}
