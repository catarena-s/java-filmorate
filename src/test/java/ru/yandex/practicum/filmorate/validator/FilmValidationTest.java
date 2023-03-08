package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.tools.TestValidatorUtil.hasErrorMessage;

class FilmValidationTest {

    @ParameterizedTest
    @CsvSource({"0,''", "-200,'   '"})
    void createWrongDurationAndName(int duration, String name) {
        Film film = Film.builder()
                .name(name)
                .duration(duration)
                .description("Description")
                .releaseDate(LocalDate.of(1900, 1, 15))
                .build();
        Assertions.assertAll(
                () -> Assertions.assertTrue(hasErrorMessage(film, "Продолжительность фильма должна быть положительной")),
                () -> Assertions.assertTrue(hasErrorMessage(film, "Отсутствует название"))
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -200})
    void createWrongDuration(int duration) {
        Film film = Film.builder()
                .name("name")
                .duration(duration)
                .description("Description")
                .releaseDate(LocalDate.of(1900, 1, 15))
                .build();
        Assertions.assertTrue(hasErrorMessage(film, "Продолжительность фильма должна быть положительной"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "                "})
    void createWrongName(String name) {
        Film film = Film.builder()
                .name(name).description("Description").duration(120)
                .releaseDate(LocalDate.of(1900, 1, 15))
                .build();
        Assertions.assertTrue(hasErrorMessage(film, "Отсутствует название"));
    }

    @Test
    void createWrongDescription() {
        Film film = Film.builder()
                .name("name").duration(120)
                .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                        "Здесь они хотят разыскать господина Огюста Куглова, " +
                        "который задолжал им деньги, а именно 20 миллионов. о Куглов, " +
                        "который за время «своего отсутствия», стал кандидатом Коломбани.")
                .releaseDate(LocalDate.of(1900, 1, 15))
                .build();
        Assertions.assertTrue(hasErrorMessage(film, "Длинна описания не должна быть больше 200 символов"));
    }

    @Test
    void createWrongReleaseDate() {
        Film film = Film.builder()
                .name("name").duration(120)
                .description("Description")
                .releaseDate(LocalDate.of(1802, 1, 15))
                .build();
        Assertions.assertTrue(hasErrorMessage(film, "Дата релиза должна быть после 1895-12-28"));
    }
}
