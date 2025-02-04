package ru.khav.NewsPaper.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

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
    @Column(name = "prefered_status")
    private boolean status;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Person user;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "theme_id", referencedColumnName = "id")
    private Themes theme;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Preferences)) return false;
        Preferences pref = (Preferences) o;
        return pref.id == this.id && pref.status == this.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }

    @Override
    public String toString() {
        return "Preferences{" +
                "id=" + id +
                ", status=" + status +
                ", user=" + user.getName() +
                ", theme=" + theme.getName() +
                '}';
    }
}
