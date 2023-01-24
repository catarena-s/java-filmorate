package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
public class UserService extends Service<User> {
    public UserService() {
        super();
        setLogger(log);
        setType("/users");
    }

    protected void validate(User user) {
//        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
//            throw new ValidationException("Некорректная почта", log);
//        }
//        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
//            throw new ValidationException("Некорректная логин", log);
//        }
//        if (user.getBirthday().isAfter(LocalDate.now())) {
//            throw new ValidationException("Некорректная дата рождения", log);
//        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
