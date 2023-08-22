package ru.green.nca.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.time.Instant;

/**
 * Класс представляет собой новостной элемент.
 * Этот класс представляет собой структуру для хранения информации о новостях.
 * Каждая новость имеет уникальный идентификатор, заголовок, текстовое содержание,
 * дату создания, дату последнего редактирования, идентификатор пользователя,
 * который добавил новость, и идентификатор пользователя, который последний раз обновил новость.
 *
 * @since 1.0
 */
@Table(name = "news")
@Entity
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class News {
    /**
     * Уникальный идентификатор новости.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Заголовок новости.
     */
    private String title;

    /**
     * Текстовое содержание новости.
     */
    @Column(name = "text", length = 2000)
    private String text;

    /**
     * Дата создания новости.
     */
    @Column(name = "creation_date")
    private Instant creationDate;

    /**
     * Дата последнего редактирования новости.
     */
    @Column(name = "last_edit_date")
    private Instant lastEditDate;

    /**
     * Идентификатор пользователя, добавившего новость.
     */
    @Column(name = "inserted_by_id")
    private Integer insertedById;

    /**
     * Идентификатор пользователя, последний раз обновившего новость.
     */
    @Column(name = "updated_by_id")
    private Integer updatedById;

}
