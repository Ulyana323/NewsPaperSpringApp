package ru.khav.NewsPaper.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class PersonDTO {

    @UniqueElements(message = "this login already exists")
    @JsonProperty("name")
    @NotNull(message = "Full it, please")
    private String name;


    @JsonProperty("email")
    @NotNull(message = "Full it, please")
    private String email;

    @JsonProperty("password")
    @NotNull(message = "Full it, please")
    private String password;
}
