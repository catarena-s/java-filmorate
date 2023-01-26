package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

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
    private LocalDate releaseDate;

}
