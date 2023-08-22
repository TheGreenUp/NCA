package ru.green.nca.dto;
import java.time.Instant;

public class CommentDto {
    private String text;
    private Instant creationDate;
    private Instant lastEditDate;
    private String authorName;
    private String authorSurname;
}
