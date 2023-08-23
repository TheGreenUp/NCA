package ru.green.nca.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
@Data
@AllArgsConstructor
public class CommentDto {
    private int id;
    private int idNews;
    private String text;
    private Instant creationDate;
    private Instant lastEditDate;
    private String authorName;
    private String authorSurname;
}
