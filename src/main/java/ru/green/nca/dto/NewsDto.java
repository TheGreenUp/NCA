package ru.green.nca.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
@Data
@AllArgsConstructor
public class NewsDto {
    private int id;
    private String title;
    private String text;
    private Instant creationDate;
    private Instant lastEditDate;
    private String authorName;
    private String authorSurname;
    private String updatedByName;
    private String updatedBySurname;

}

