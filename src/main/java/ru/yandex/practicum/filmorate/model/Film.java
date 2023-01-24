package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
public class Film extends FilmorateObject {
    @NotNull(message = "Отсутствует название")
    @NotBlank(message = "Название не должно быть пустым")
    private final String name;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private final int duration;

    @Size(max = 200, message = "Длинна описания не должна быть больше 200 символов")
    private final String description;

    @ReleaseDate
    private final LocalDate releaseDate;

}
