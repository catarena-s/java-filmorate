package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ru.yandex.practicum.filmorate.validator.NotContainSpace;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
public class User extends FilmorateObject {
    @NotNull(message = "Login должен быть заполнен")
    @NotBlank(message = "Login не может быть пустой строкой")
    @NotContainSpace()
    private final String login;

    @NotBlank(message = "Email должен быть заполнен")
    @Email(message = "Email не корректный")
    private final String email;

    private String name = "";

    @Past(message = "Некорректная дата рождения")
    private final LocalDate birthday;
}
