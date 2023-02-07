package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.FilmorateObject;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserControllerTest extends ControllerTest {

    @BeforeAll
    static void beforeAll() {
        endPoint = "/users";
        initCorrectTestDataForCreate();
        initCorrectTestDataForUpdate();
        initWrongTestDataForUpdate();
        initWrongTestDataForCreate();
    }

    @Test
    void createWithoutName() {
        User user = User.builder()
                .login("Login")
                .email("af@mail.ru")
                .birthday(LocalDate.of(1900, 1, 15))
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity(endPoint, user, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        FilmorateObject fromJson = gson.fromJson(response.getBody(), User.class);
        assertNotNull(fromJson);
        user.setName("Login");
        assertEquals(user, fromJson);

    }

    private static void initWrongTestDataForCreate() {
        testDataWithErrForCreate.add(new TestData(
                "{\"birthday\":\"'2200-01-15' -> Некорректная дата рождения\"," +
                        "\"login\":\"'' -> Login должен быть заполнен\"," +
                        "\"email\":\"'' -> Email должен быть заполнен\"}",
                HttpStatus.BAD_REQUEST,
                User.class,
                User.builder().login("").email("").name("Name")
                        .birthday(LocalDate.of(2200, 1, 15))
                        .build())
        );

        testDataWithErrForCreate.add(new TestData(
                "{\"login\":\"'Log in' -> Login не должен содержать пробелы\"}",
                HttpStatus.BAD_REQUEST,
                User.class,
                User.builder().login("Log in").email("mail@mail.com").name("Name")
                        .birthday(LocalDate.of(1995, 1, 12))
                        .build()
        ));

        testDataWithErrForCreate.add(new TestData(
                "{\"login\":\"'' -> Login должен быть заполнен\"," +
                        "\"email\":\"'mail.com' -> Email не корректный\"}",
                HttpStatus.BAD_REQUEST,
                User.class,
                User.builder().login("").email("mail.com")
                        .birthday(LocalDate.of(1995, 1, 12))
                        .build())
        );
    }

    private static void initWrongTestDataForUpdate() {
        User userUpdateWrongId = User.builder()
                .login("Login")
                .email("af@h.ru")
                .name("User Name")
                .birthday(LocalDate.of(1900, 1, 15))
                .build();
        userUpdateWrongId.setId(9999);

        testDataWithErrForUpdate.add(new TestData(
                HttpStatus.NOT_FOUND,
                User.class,
                userUpdateWrongId)
        );
    }

    private static void initCorrectTestDataForUpdate() {
        User userUpdateCorrect = User.builder()
                .login("Login")
                .email("af@h.ru")
                .name("User Name")
                .birthday(LocalDate.of(1900, 1, 15))
                .build();
        userUpdateCorrect.setId(1);

        testDataCorrectForUpdate.add(new TestData(
                userUpdateCorrect, HttpStatus.OK,
                User.class, userUpdateCorrect));
    }

    private static void initCorrectTestDataForCreate() {
        User user = User.builder()
                .login("Login")
                .email("af@h.ru")
                .name("User Name")
                .birthday(LocalDate.of(1900, 1, 15))
                .build();
        user.setId(1);

        testDataCorrectForCreate.add(new TestData(
                user, HttpStatus.CREATED,
                User.class,
                User.builder()
                        .login("Login")
                        .email("af@h.ru")
                        .name("User Name")
                        .birthday(LocalDate.of(1900, 1, 15))
                        .build()
        ));
    }


}
