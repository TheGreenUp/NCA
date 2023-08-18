package ru.green.nca.model;

import lombok.Data;
import ru.green.nca.entity.Comments;
import ru.green.nca.entity.News;

import java.util.List;
@Data
public class NewsWithComments {
    private News news;
    private List<Comments> comments;

    public NewsWithComments(News news, List<Comments> comments) {
        this.news = news;
        this.comments = comments;
    }
}
