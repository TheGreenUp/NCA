package ru.green.nca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.News;

import java.util.List;
@Data
@AllArgsConstructor
public class NewsWithCommentsDto {
    private News news;
    private List<Comment> comments;

}
