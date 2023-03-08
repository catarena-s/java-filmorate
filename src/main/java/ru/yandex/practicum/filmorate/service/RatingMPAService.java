package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.RatingMPAStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingMPAService {
    private final RatingMPAStorage storage;

    public Collection<RatingMPA> getAll() {
        return storage.getAll();
    }

    public RatingMPA getById(int id) {
        return storage.getById(id);
    }
}
