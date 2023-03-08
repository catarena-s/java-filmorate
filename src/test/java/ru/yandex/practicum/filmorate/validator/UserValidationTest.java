package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.tools.TestValidatorUtil.hasErrorMessage;

class UserValidationTest {

    @ParameterizedTest
    @ValueSource(strings = {"' '", "'   '", "Log in", "   login", "Login   "})
    void createNotWrongLogin(String login) {
        User newUser = User.builder().login(login).email("email@mail.com").name("Name")
                .birthday(LocalDate.of(1986, 1, 15))
                .build();
        Assertions.assertTrue(hasErrorMessage(newUser, "Login не должен содержать пробелы"));
    }

    @Test
    void createNotWrongLogin2() {
        User newUser = User.builder().login("").email("").name("Name")
                .birthday(LocalDate.of(1986, 1, 15))
                .build();
        Assertions.assertAll(
                () -> Assertions.assertTrue(hasErrorMessage(newUser, "Login должен быть заполнен")),
                () -> Assertions.assertTrue(hasErrorMessage(newUser, "Email должен быть заполнен"))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"' '", "'   '", "email @mail.com", "mail.com", "@mail@com"})
    void createNotWrongLogin4(String email) {
        User newUser = User.builder().login("login").email(email).name("Name")
                .birthday(LocalDate.of(1986, 1, 15))
                .build();
        Assertions.assertTrue(hasErrorMessage(newUser, "Email не корректный"));
    }

    @Test
    void createNotWrongLogin3() {
        User newUser = User.builder().login("Login").email("mail@mail.com").name("Name")
                .birthday(LocalDate.of(2200, 1, 15))
                .build();

        Assertions.assertTrue(hasErrorMessage(newUser, "Некорректная дата рождения"));
    }
}
