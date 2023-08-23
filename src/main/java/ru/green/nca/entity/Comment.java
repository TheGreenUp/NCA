package ru.green.nca.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Table(name = "comments")
@Entity

@Slf4j
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
    @CreatedDate
    private Instant creationDate;
    @Column(name = "last_edit_date")
    @LastModifiedDate
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
