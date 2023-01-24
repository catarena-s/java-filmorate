package ru.yandex.practicum.filmorate.controller;

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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmorateObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FilmControllerTest extends ControllerTest<FilmController>{

    @BeforeAll
    static void beforeAll() {
        controller = new FilmController();
        endPoint = "/films";
        initCorrectTestDataForCreate();
        initCorrectTestDataForUpdate();
        initWrongTestDataForUpdate();
        initWrongTestDataForCreate();
    }

    private static void initWrongTestDataForCreate() {
        testDataWithErrForCreate.add(new TestData(
                "{\"duration\":\"'0' -> Продолжительность фильма должна быть положительной\"," +
                        "\"name\":\"'' -> Название не должно быть пустым\"}",
                null,
                HttpStatus.BAD_REQUEST,
                Film.class,
                new Film("", 0, "sfg", LocalDate.of(1900, 1, 15)))
        );
        testDataWithErrForCreate.add(new TestData(
                "{\"duration\":\"'-200' -> Продолжительность фильма должна быть положительной\"}",
                null,
                HttpStatus.BAD_REQUEST,
                Film.class,
                new Film("New film", -200, "name", LocalDate.of(1900, 1, 15)))
        );

        testDataWithErrForCreate.add(new TestData(
                "{\"description\":\"'Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                        "Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, " +
                        "а именно 20 миллионов. о Куглов, " +
                        "который за время «своего отсутствия», стал кандидатом Коломбани.' " +
                        "-> Длинна описания не должна быть больше 200 символов\"}",
                null,
                HttpStatus.BAD_REQUEST,
                Film.class,
                new Film("Film", 100, "Пятеро друзей ( комик-группа «Шарло»), " +
                        "приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, " +
                        "который задолжал им деньги, а именно 20 миллионов. о Куглов, " +
                        "который за время «своего отсутствия», стал кандидатом Коломбани.",
                        LocalDate.of(1900, 1, 15)))
        );
    }

    private static void initWrongTestDataForUpdate() {
        Film userUpdateWrongId = new Film("Film", 200, "af", LocalDate.of(1900, 1, 15));
        userUpdateWrongId.setId(9999);
        testDataWithErrForUpdate.add(new TestData(
                "",
                null,
                HttpStatus.NOT_FOUND,
                Film.class,
                userUpdateWrongId)
        );
    }

    private static void initCorrectTestDataForUpdate() {
        Film userUpdateCorrect = new Film("Film", 200, "af", LocalDate.of(1900, 1, 15));
        userUpdateCorrect.setId(1);
        testDataCorrectForUpdate.add(new TestData("", userUpdateCorrect, HttpStatus.OK,
                Film.class, userUpdateCorrect));
    }

    private static void initCorrectTestDataForCreate() {
        Film user = new Film("Film", 200, "af", LocalDate.of(1900, 1, 15));
        user.setId(1);
        testDataCorrectForCreate.add(new TestData("", user, HttpStatus.OK,
                Film.class,
                new Film("Film", 200, "af", LocalDate.of(1900, 1, 15)))
        );
    }


}
