package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ru.yandex.practicum.filmorate.validator.NotContainSpace;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@ToString(callSuper = true)
@Builder
public class User extends FilmorateObject {
    @NotBlank(message = "Login должен быть заполнен")
    @NotContainSpace(message = "Login не должен содержать пробелы")
    private final String login;

    @NotBlank(message = "Email должен быть заполнен")
    @Email(message = "Email не корректный")
    private final String email;

    private String name;

    @Past(message = "Некорректная дата рождения")
    private LocalDate birthday;


}
