package ru.khav.NewsPaper.utill;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.khav.NewsPaper.DTO.NewsDTO;


@Component
public class NewsValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return NewsDTO.class.equals(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
        NewsDTO news = (NewsDTO) target;
        // Проверка title
        if (news.getTitle() == null || news.getTitle().isEmpty()) {
            errors.rejectValue("title", "field.required", "Title is required");
        }
        // Проверка text
        if (news.getText() == null || news.getText().isEmpty()) {
            errors.rejectValue("text", "field.required", "Text is required");
        }

    }
}
