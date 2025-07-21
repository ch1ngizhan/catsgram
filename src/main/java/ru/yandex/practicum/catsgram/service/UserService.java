package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public Optional<User> findById(long userId) {
        if (users.containsKey(userId)) {
            return Optional.ofNullable(users.get(userId));
        }
        return Optional.empty();
    }

    public User create(User user) {
        // Проверка обязательных условий
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Email должен быть указан");
        }

        // Проверка уникальности email
        boolean emailExists = users.values()
                .stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()));

        if (emailExists) {
            throw new DuplicatedDataException("Этот email уже используется");
        }

        // Установка дополнительных полей
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());

        // Сохранение пользователя
        users.put(user.getId(), user);
        return user;
    }

    public User update(User newUser) {
        // Проверка обязательных полей
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("ID должен быть указан");
        }

        // Поиск существующего пользователя
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            // Проверка и обновление email
            if (newUser.getEmail() != null && !newUser.getEmail().equals(oldUser.getEmail())) {
                boolean emailExists = users.values()
                        .stream()
                        .anyMatch(u -> u.getEmail().equals(newUser.getEmail()));
                if (emailExists) {
                    throw new DuplicatedDataException("Этот email уже используется");
                }
                oldUser.setEmail(newUser.getEmail());
            }

            // Обновление остальных полей
            if (newUser.getUsername() != null) {
                oldUser.setUsername(newUser.getUsername());
            }

            if (newUser.getPassword() != null) {
                oldUser.setPassword(newUser.getPassword());
            }

            return oldUser;
        }

        throw new NotFoundException("Пользователь с ID = " + newUser.getId() + " не найден");
    }

    // Вспомогательный метод для генерации ID нового пользователя
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Optional<User> findUserById(Long id) {
        Optional<User> user;
        if (users.containsKey(id)) {
            user = Optional.of(users.get(id));
            return user;
        }
        user = Optional.empty();
        return user;
    }
}
