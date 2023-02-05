package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validator.NotContainSpace;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString(callSuper = true)
@Builder
public class User extends FilmorateObject {
    @NotBlank(message = "Login должен быть заполнен")
    @NotContainSpace(message = "Login не должен содержать пробелы")
    private final String login;
    private String name;

    @NotBlank(message = "Email должен быть заполнен")
    @Email(message = "Email не корректный")
    private final String email;

    @Past(message = "Некорректная дата рождения")
    private LocalDate birthday;
    /** Список друзей **/
    private final Set<Long> friends = new HashSet<>();
}
