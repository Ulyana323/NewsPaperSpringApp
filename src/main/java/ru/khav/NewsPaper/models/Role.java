
package ru.khav.NewsPaper.models;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "role_name")
    private String Role_name;

    @Transient
    @OneToMany(mappedBy = "role")
    private Set<Person> users;

    public Role() {
    }

    public Role(int id) {
        this.id = id;
    }

    public Role(int id, String name) {
        this.id = id;
        this.Role_name = name;
    }

    @Override
    public String getAuthority() {
        return getRole_name();
    }
}
