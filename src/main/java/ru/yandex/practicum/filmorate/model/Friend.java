package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Getter
@EqualsAndHashCode(exclude = "isConfirmed")
public class Friend {
    private long userId;
    private boolean isConfirmed;
}
