package ru.green.nca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Table(name = "comments")
@Entity

@Slf4j
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "text", length = 300)
    private String text;
    @Column(name = "creation_date")
    private Instant creationDate;
    @Column(name = "last_edit_date")
    private Instant lastEditDate;
    @Column(name = "inserted_by_id")
    private Integer insertedById;
    @Column(name = "id_news")
    private Integer idNews;

    @PrePersist
    public void prePersist() {
        creationDate = Instant.now();
        lastEditDate = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        lastEditDate = Instant.now();
    }
}
