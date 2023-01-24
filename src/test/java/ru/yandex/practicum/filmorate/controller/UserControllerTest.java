package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.model.FilmorateObject;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.tools.GsonAdapter;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest extends ControllerTest<UserController>  {

    @BeforeAll
    static void beforeAll() {
        controller = new UserController();
        endPoint = "/users";
        initCorrectTestDataForCreate();
        initCorrectTestDataForUpdate();
        initWrongTestDataForUpdate();
        initWrongTestDataForCreate();
    }

    private static void initWrongTestDataForCreate() {
        testDataWithErrForCreate.add(new TestData(
                "{\"birthday\":\"'2200-01-15' -> Некорректная дата рождения\"," +
                        "\"login\":\"'' -> Login не может быть пустой строкой\"," +
                        "\"email\":\"'' -> Email должен быть заполнен\"}",
                null,
                HttpStatus.BAD_REQUEST,
                User.class, new User("", "", "sfg", LocalDate.of(2200, 1, 15)))
        );
        testDataWithErrForCreate.add(new TestData(
                "{\"login\":\"'' -> Login не может быть пустой строкой\"}",
                null,
                HttpStatus.BAD_REQUEST,
                User.class, new User("", "mail@mail.com", "name", LocalDate.of(1900, 1, 15)))
        );

        testDataWithErrForCreate.add(new TestData(
                "{\"login\":\"'' -> Login не может быть пустой строкой\"," +
                        "\"email\":\"'mail.com' -> Email не корректный\"}",
                null,
                HttpStatus.BAD_REQUEST,
                User.class, new User("", "mail.com", "", LocalDate.of(1900, 1, 15)))
        );
    }

    private static void initWrongTestDataForUpdate() {
        User userUpdateWrongId = new User("Login", "af@h.ru", "af", LocalDate.of(1900, 1, 15));
        userUpdateWrongId.setId(9999);
        testDataWithErrForUpdate.add(new TestData(
                "",
                null,
                HttpStatus.NOT_FOUND,
                User.class,
                userUpdateWrongId)
        );
    }

    private static void initCorrectTestDataForUpdate() {
        User userUpdateCorrect = new User("Login", "af@mail.ru", "User Name",
                LocalDate.of(1900, 1, 15));
        userUpdateCorrect.setId(1);
        testDataCorrectForUpdate.add(new TestData("", userUpdateCorrect, HttpStatus.OK,
                User.class, userUpdateCorrect));
    }

    private static void initCorrectTestDataForCreate() {
        User user = new User("sf", "af@h.ru", "af", LocalDate.of(1900, 1, 15));
        user.setId(1);
        testDataCorrectForCreate.add(new TestData("", user, HttpStatus.OK,
                User.class,
                new User("sf", "af@h.ru", "af", LocalDate.of(1900, 1, 15)))
        );
    }


}
