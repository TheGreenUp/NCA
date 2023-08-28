package ru.green.nca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {
    private Integer id;
    private String title;
    private String text;
    private Instant creationDate;
    private Instant lastEditDate;
    private String authorName;
    private String authorSurname;
    private String updatedByName;
    private String updatedBySurname;

}

