package ru.yandex.practicum.catsgram.model;

import lombok.*;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = {"id"})
public class Post {
    private Long id;//уникальный идентификатор сообщения

    private long authorId;//пользователь, который создал сообщение

    private String description;//текстовое описание сообщения

    private Instant postDate;//дата и время создания сообщения
}
