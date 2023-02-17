package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString(callSuper = true)
@Builder
public class Film extends FilmorateObject {
    @NotBlank(message = "Отсутствует название")
    private String name;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    @Size(max = 200, message = "Длинна описания не должна быть больше 200 символов")
    private String description;

    @ReleaseDate
    private LocalDate releaseDate;//  дата релиза

    private final Set<Long> likes = new HashSet<>();//список лайков(id пользователей поставивших лайк)
}
