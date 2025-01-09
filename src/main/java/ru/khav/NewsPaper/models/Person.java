package ru.khav.NewsPaper.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    @NotNull
    private String name;

    @Column(name="lastname")
    @NotNull
    private String lastname;


    @Column(name="email")
    @NotNull
    private String email;

    @Column(name="password")
    private String password;

@OneToMany(mappedBy = "owner",fetch = FetchType.EAGER)
    private List<Comment> comments;
}
