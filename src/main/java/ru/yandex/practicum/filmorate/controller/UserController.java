package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {

    @Autowired
    public UserController(UserService service) {
        super(service);
    }

    @Override
    public Collection<User> getAll() {
        log.debug("Получен запрос GET '/users'");
        return super.getAll();
    }

    @Override
    public User create(User obj) {
        log.debug("Получен запрос POST '/users' :");
        log.debug("добавить : {}", obj);
        return super.create(obj);
    }

    @Override
    public User update(User obj) {
        log.debug("Получен запрос PUT '/users' :");
        log.debug("новое значение : {}", obj);
        return super.update(obj);
    }

    @Override
    public User getById(@PathVariable(name = "id") long id) {
        log.debug("Получен запрос GET '/users/{}' :", id);
        return super.getById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable(name = "id") long userId,
                          @PathVariable(name = "friendId") long friendId) {
        log.debug("Получен PUT запрос '/users/{}/friends/{}'", userId, friendId);
        return ((UserService) service).addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User removeFriend(@PathVariable(name = "id") long userId,
                             @PathVariable(name = "friendId") long friendId) {
        log.debug("Получен DELETE запрос '/users/{}/friends/{}'", userId, friendId);
        return ((UserService) service).removeFromFriends(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable(name = "id") long userId) {
        log.debug("Получен запрос GET '/users/{}/friends'", userId);
        return ((UserService) service).getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable(name = "id") long userId,
                                       @PathVariable(name = "otherId") long otherId) {
        log.debug("Получен запрос GET '/users/{}/friends/common/{}'", userId, otherId);
        return ((UserService) service).getCommonFriends(userId, otherId);
    }
}
