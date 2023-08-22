package ru.green.nca.model;

import lombok.Data;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.News;

import java.util.List;
@Data
public class NewsWithComments {
    private News news;
    private List<Comment> comments;

    public NewsWithComments(News news, List<Comment> comments) {
        this.news = news;
        this.comments = comments;
    }
}
