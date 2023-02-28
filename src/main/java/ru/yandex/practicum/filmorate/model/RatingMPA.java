package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode
public class RatingMPA extends FilmorateObject {
    private String name;

    @Builder(toBuilder = true)
    public RatingMPA(long id, String name) {
        super(id);
        this.name = name;
    }
}
