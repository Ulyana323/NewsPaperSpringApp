package ru.khav.NewsPaper.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "lastname")
    @NotNull
    private String lastname;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToMany(mappedBy = "personOwnLike", fetch = FetchType.EAGER)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Preferences> preferences = new HashSet<>();


    public Person(int i, String name, String lastname, String email, String number) {
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Collections.singletonList(new SimpleGrantedAuthority(this.getRole().getRoleName()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person pers = (Person) o;
        return pers.id == this.id && pers.email.equals(this.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastname, email);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
