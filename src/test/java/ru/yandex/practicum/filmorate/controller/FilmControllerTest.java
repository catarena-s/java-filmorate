package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.time.LocalDate;
import java.util.HashSet;

public class FilmControllerTest extends ControllerTest {


    @BeforeAll
    static void beforeAll() {
        endPoint = "/films";
        initCorrectTestDataForCreate();
        initCorrectTestDataForUpdate();
        initWrongTestDataForUpdate();
        initWrongTestDataForCreate();
    }

    private static void initWrongTestDataForCreate() {
        RatingMPA mpa = new RatingMPA(1,"G");
        testDataWithErrForCreate.add(new TestData(
                        "{\"duration\":\"'0' -> Продолжительность фильма должна быть положительной\"," +
                                "\"name\":\"'' -> Отсутствует название\"}",
                        HttpStatus.BAD_REQUEST,
                        Film.class,
                        Film.builder()
                                .name("")
                                .duration(0)
                                .description("Description")
                                .releaseDate(LocalDate.of(1900, 1, 15))
                                .mpa(mpa)
                                .likes(new HashSet<>())
                                .build()
                )
        );
        testDataWithErrForCreate.add(new TestData(
                        "{\"duration\":\"'-200' -> Продолжительность фильма должна быть положительной\"}",
                        HttpStatus.BAD_REQUEST,
                        Film.class,
                        Film.builder()
                                .name("Name film")
                                .duration(-200)
                                .description("Description")
                                .releaseDate(LocalDate.of(1900, 1, 15))
                                .mpa(mpa)
                                .likes(new HashSet<>())
                                .build()
                )
        );
        testDataWithErrForCreate.add(new TestData(
                        "{\"name\":\"'null' -> Отсутствует название\"}",
                        HttpStatus.BAD_REQUEST,
                        Film.class,
                        Film.builder()
                                .duration(200)
                                .description("Description")
                                .releaseDate(LocalDate.of(1900, 1, 15))
                                .mpa(mpa)
                                .likes(new HashSet<>())
                                .build()
                )
        );
        testDataWithErrForCreate.add(new TestData(
                        "{\"description\":\"'Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                                "Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, " +
                                "а именно 20 миллионов. о Куглов, " +
                                "который за время «своего отсутствия», стал кандидатом Коломбани.' " +
                                "-> Длинна описания не должна быть больше 200 символов\"}",
                        HttpStatus.BAD_REQUEST,
                        Film.class,
                        Film.builder()
                                .name("Name film")
                                .duration(100)
                                .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                                        "Здесь они хотят разыскать господина Огюста Куглова, " +
                                        "который задолжал им деньги, а именно 20 миллионов. о Куглов, " +
                                        "который за время «своего отсутствия», стал кандидатом Коломбани.")
                                .releaseDate(LocalDate.of(1900, 1, 15))
                                .mpa(mpa)
                                .likes(new HashSet<>())
                                .build()
                )
        );
    }

    private static void initWrongTestDataForUpdate() {
        RatingMPA mpa = new RatingMPA(1,"G");
        Film filmUpdateWrongId = Film.builder()
                .name("Updated film")
                .duration(200)
                .description("Updated Description")
                .releaseDate(LocalDate.of(1900, 1, 15))
                .mpa(mpa)
                .likes(new HashSet<>())
                .build();
        filmUpdateWrongId.setId(9999);
        testDataWithErrForUpdate.add(new TestData(
                HttpStatus.NOT_FOUND,
                Film.class,
                filmUpdateWrongId)
        );
    }

    private static void initCorrectTestDataForUpdate() {
        RatingMPA mpa = new RatingMPA(1,"G");
        Film filmUpdateCorrect = Film.builder()
                .name("Updated film")
                .duration(200)
                .description("Updated Description")
                .releaseDate(LocalDate.of(1900, 1, 15))
                .mpa(mpa)
                .likes(new HashSet<>())
//                .
                .build();
        filmUpdateCorrect.setId(3);
        testDataCorrectForUpdate.add(new TestData(
                filmUpdateCorrect,
                HttpStatus.OK,
                Film.class, filmUpdateCorrect));
    }

    private static void initCorrectTestDataForCreate() {
        RatingMPA mpa = new RatingMPA(1,"G");
        Film film = Film.builder()
                .name("Name film")
                .duration(200)
                .description("Description")
                .releaseDate(LocalDate.of(1900, 1, 15))
                .mpa(mpa)
                .likes(new HashSet<>())
                .build();
        film.setId(3);
        testDataCorrectForCreate.add(new TestData(
                        film,
                        HttpStatus.CREATED,
                        Film.class,
                        Film.builder()
                                .name("Name film")
                                .duration(200)
                                .description("Description")
                                .releaseDate(LocalDate.of(1900, 1, 15))
                                .mpa(mpa)
                                .likes(new HashSet<>())
                                .build()
                )
        );
    }


}
