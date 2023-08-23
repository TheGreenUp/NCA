package ru.green.nca.service;

import ru.green.nca.dto.NewsDto;
import ru.green.nca.entity.News;
import ru.green.nca.dto.NewsWithCommentsDto;

import java.util.List;

public interface NewsService {
    News getNewsById(int newsId);

    List<News> getNews(int page, int size);

    News createNews(NewsDto newsDto);

    NewsWithCommentsDto viewNewsWithComments(int newsId, int commentPage, int commentSize);

    News updateNews(int newsId, NewsDto updatedNewsDto);

    void deleteNews(int newsId);

    List<News> searchByTitleOrText(String keyword, int page, int size);

}
