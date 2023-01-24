package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FilmService extends Service<Film> {
    public FilmService() {
        super();
        setLogger(log);
        setType("/films");
    }

//    protected void validate(Film obj) {
//        List<String> error = new ArrayList<>();
//        String format = "%s = %s";
//        if (obj.getName().isBlank()) {
//            error.add(String.format(format,"Name", "Отсутствует название"));
//        }
//        if (obj.getDescription().length() > 200) {
//            error.add(String.format("Description", "Длинное описание"));
//        }
////        throw new ValidationException("Длинное описание", log);
//        if (obj.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
//            throw new ValidationException("Некорректная дата", log);
//        }
//        if (obj.getDuration() < 0) {
//            throw new ValidationException("продолжительность фильма должна быть положительной", log);
//        }
//        if (error.isEmpty()) return;
//        StringBuilder errBuilder = new StringBuilder();
////        for (Map.Entry<String, String> item : error.entrySet()) {
////                errBuilder.append()
////        }
//        throw new ValidationException("Отсутствует название", log);
//    }
}
