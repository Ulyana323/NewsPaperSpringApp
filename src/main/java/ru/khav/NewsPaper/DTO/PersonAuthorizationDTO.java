package ru.khav.NewsPaper.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonAuthorizationDTO {

    @JsonProperty("email")
    @NotNull(message = "Full it, please")
    private String email;

    @JsonProperty("password")
    @NotNull(message = "Full it, please")
    private String password;
}
