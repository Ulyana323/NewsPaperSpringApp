package ru.khav.NewsPaper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.khav.NewsPaper.DTO.CommentShowDTO;
import ru.khav.NewsPaper.models.Comment;
import ru.khav.NewsPaper.models.Person;


@SpringBootApplication
public class NewsPaperApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsPaperApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();

	}
}
