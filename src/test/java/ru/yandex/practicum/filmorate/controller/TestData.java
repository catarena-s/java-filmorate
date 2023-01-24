package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.model.FilmorateObject;

import java.lang.reflect.Type;
import java.net.URI;

@Getter
@AllArgsConstructor
public class TestData {
    private final String expectedBody;
    private final FilmorateObject expectedObj;
    private final HttpStatus responseStatusCode;
    private final Class type;
    private final FilmorateObject obj;
}
