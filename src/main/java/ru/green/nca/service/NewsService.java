package ru.green.nca.service;

import ru.green.nca.dto.NewsDto;
import ru.green.nca.dto.NewsWithCommentsDto;

import java.util.List;

public interface NewsService {
    NewsDto getNewsById(int newsId);

    List<NewsDto> getNews(int page, int size);

    NewsDto createNews(NewsDto newsDto);

    NewsWithCommentsDto viewNewsWithComments(int newsId, int commentPage, int commentSize);

    NewsDto updateNews(int newsId, NewsDto updatedNewsDto);

    void deleteNews(int newsId);

    List<NewsDto> searchByTitleOrText(String keyword, int page, int size);

}
