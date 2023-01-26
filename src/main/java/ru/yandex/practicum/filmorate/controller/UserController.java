package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {
    protected UserController() {
        setService(new UserService());
    }

    @Override
    public Collection<User> getAll() {
        log.info("Получен запрос GET /users");
        return super.getAll();
    }

    @Override
    public User create(User obj) {
        log.info("Получен запрос POST /users :");
        log.info("добавить : {}", obj);
        return super.create(obj);
    }

    @Override
    public User update(User obj) {
        log.info("Получен запрос PUT /users :");
        log.info("новое значение : {}", obj);
        return super.update(obj);
    }
}
