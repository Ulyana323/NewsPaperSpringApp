package ru.khav.NewsPaper.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class PersonRegistrationDTO {

    @JsonProperty("name")
    @NotNull(message = "Full it, please")
    private String name;


    @JsonProperty("lastname")
    @NotNull(message = "Full it, please")
    private String lastname;

    @UniqueElements(message = "this email already exists")
    @JsonProperty("email")
    @NotNull(message = "Full it, please")
    private String email;

    @JsonProperty("password")
    @NotNull(message = "Full it, please")
    private String password;
}
