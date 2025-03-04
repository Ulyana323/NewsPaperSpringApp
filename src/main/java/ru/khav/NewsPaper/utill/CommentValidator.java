package ru.khav.NewsPaper.utill;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.khav.NewsPaper.DTO.CommentDTO;

@Component
public class CommentValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CommentDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
CommentDTO comment=(CommentDTO) target;

if(comment.getText().isEmpty())
{
    errors.rejectValue("text","","Comment can't be empty!");
}
    }
}
