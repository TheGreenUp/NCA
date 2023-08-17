package ru.green.nca.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
@Table(name = "news")
@Entity

@Slf4j
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "text")
    private String text;
    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "last_edit_date")
    private Date lastEditDate;
    @Column(name = "inserted_by_id")
    private Integer insertedById;
    @Column(name = "updated_by_id")
    private Integer updatedById;
}
