package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage extends AbstractStorage<User> implements UserStorage {

    public InMemoryUserStorage() {
        super();
        setLog(log);
        setStorageType("User");
    }

    /**
     * Получить список пользователей по набору id
     *
     * @param set список id пользователей
     */
    @Override
    public List<User> getUsers(Set<Long> set) {
        return map.entrySet().stream()
                .filter(x -> set.contains(x.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    /**
     * Получить друзей пользователя
     *
     * @param userId - id пользователя
     * @return - список друзей
     */
    @Override
    public List<User> getFriends(long userId) {
        Set<Long> friends = map.get(userId).getFriends();
        return map.entrySet().stream()
                .filter(m -> friends.contains(m.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
