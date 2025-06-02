package ru.yandex.practicum.catsgram.model;

import lombok.*;

import java.time.Instant;
@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {

   private Long id;//уникальный идентификатор пользователя,

    private String username;//имя пользователя

    private String email;//электронная почта пользователя

    private String password;// пароль пользователя

    private Instant registrationDate;//дата и время регистрации
}
