package ru.otus.dao;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByLogin(String login);
}