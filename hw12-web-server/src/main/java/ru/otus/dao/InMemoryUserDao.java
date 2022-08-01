package ru.otus.dao;

import java.util.Map;
import java.util.Optional;

public class InMemoryUserDao implements UserDao {

    private final Map<Long, User> USERS_MAP = Map.of(
            1L, new User(1L, "Администратор", "admin", "admin")
    );

    @Override
    public Optional<User> findByLogin(String login) {
        return USERS_MAP.values().stream().filter(v -> v.login().equals(login)).findFirst();
    }
}
