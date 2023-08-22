package ru.green.nca.dto;

import java.util.List;

public class NewsWithCommentsDto {
    private NewsDto news;
    private List<CommentDto> comments;

    public NewsWithCommentsDto(NewsDto news, List<CommentDto> comments) {
        this.news = news;
        this.comments = comments;
    }
}
