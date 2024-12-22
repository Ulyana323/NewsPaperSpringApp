package ru.khav.NewsPaper.utill;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.khav.NewsPaper.DTO.PersonDTO;


@Component
public class PersonValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PersonDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonDTO person = (PersonDTO) target;

        if (person.getEmail() == null) {
            errors.rejectValue("email", "Email cant be empty");
        }

    }

}
