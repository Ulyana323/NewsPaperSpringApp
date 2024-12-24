//package ru.khav.NewsPaper.utill;
//
//
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.Errors;
//import org.springframework.validation.Validator;
//import ru.khav.NewsPaper.DTO.PersonAuthorizationDTO;
//import ru.khav.NewsPaper.DTO.PersonRegistrationDTO;
//
//
//@Component
//public class PersonValidator implements Validator {
//    @Override
//    public boolean supports(Class<?> clazz) {
//        return PersonAuthorizationDTO.class.equals(clazz)
//                || PersonRegistrationDTO.class.equals(clazz);
//    }
//
//    @Override
//    public void validate(Object target, Errors errors) {
//        if(PersonAuthorizationDTO.class.equals(target))
//        PersonDTO person = (PersonDTO) target;
//
//        if (person.getEmail() == null) {
//            errors.rejectValue("email", "Email cant be empty");
//        }
//
//    }
//
//}
