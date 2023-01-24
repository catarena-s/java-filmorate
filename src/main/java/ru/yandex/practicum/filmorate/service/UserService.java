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

    @Override
    protected void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
