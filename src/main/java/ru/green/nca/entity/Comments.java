package ru.green.nca.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Table(name = "comments")
@Entity

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String text;
    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "last_edit_date")
    private Date lastEditDate;
    @Column(name = "inserted_by_id")
    private Integer insertedById;
    @Column(name = "id_news")
    private Integer idNews;
}
