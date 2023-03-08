package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Genre extends FilmorateObject {
    private String name;

    @Builder(toBuilder = true)
    public Genre(long id, String name) {
        super(id);
        this.name = name;
    }
}
