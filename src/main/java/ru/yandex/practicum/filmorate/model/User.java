package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validator.NotContainSpace;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString(callSuper = true)

public class User extends FilmorateObject {
    @NotBlank(message = "Login должен быть заполнен")
    @NotContainSpace(message = "Login не должен содержать пробелы")
    private String login;
    private String name;

    @NotBlank(message = "Email должен быть заполнен")
    @Email(message = "Email не корректный")
    private String email;

    @Past(message = "Некорректная дата рождения")
    private LocalDate birthday;

    /** Список друзей **/
    private Set<Friend> friends = new HashSet<>();

    @Builder(toBuilder = true)
    public User(long id, String login, String name, String email, LocalDate birthday) {
        super(id);
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    public User updateData(User obj) {
        setName(obj.getName());
        setBirthday(obj.getBirthday());
        setLogin(obj.getLogin());
        setEmail(obj.getEmail());
        return this;
    }

    public void addFriend(Long fiendId) {
        friends.add(new Friend(fiendId, false));
    }

    public void removeFriendById(Long friendId) {
        friends.removeIf(friend -> friend.getUserId() == friendId);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("login", login);
        values.put("email", email);
        values.put("birthday", birthday);
        return values;
    }
}
