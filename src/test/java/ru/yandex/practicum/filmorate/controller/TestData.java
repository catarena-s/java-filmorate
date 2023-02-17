package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.model.FilmorateObject;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class TestData {
    private String expectedBody;
    private FilmorateObject expectedObj;
    private final HttpStatus responseStatusCode;
    private final Class aClass;
    private final FilmorateObject obj;

    public TestData(String expectedBody, HttpStatus responseStatusCode, Class aClass, FilmorateObject obj) {
        this(responseStatusCode,aClass,obj);
        this.expectedBody = expectedBody;
    }

    public TestData(FilmorateObject expectedObj, HttpStatus responseStatusCode, Class aClass, FilmorateObject obj) {
        this(responseStatusCode,aClass,obj);
        this.expectedObj = expectedObj;
    }
}
