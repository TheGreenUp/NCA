package ru.green.nca.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.News;
import ru.green.nca.exceptions.NoDataFoundException;
import ru.green.nca.exceptions.ResourceNotFoundException;
import ru.green.nca.model.NewsWithComments;
import ru.green.nca.repository.CommentRepository;
import ru.green.nca.repository.NewsRepository;
import ru.green.nca.service.NewsService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {

    private NewsRepository newsRepository;
    private CommentRepository commentRepository;


    @Override
    public News getNewsById(int newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News with id = " + newsId + " was not found"));

    }

    @Override
    public List<News> getNews(int page, int size) {
        //TODO добавить комменты про пагинацию
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        Page<News> newsPage = newsRepository.findAll(pageable);
        if (newsPage.isEmpty()) {
            log.warn("No news was found");
            throw new NoDataFoundException("No news was found");
        }
        return newsPage.getContent();
    }

    @Override
    public News createNews(News news) {
        newsRepository.save(news);
        log.debug("Create news request with next params: " + news);
        return news;
    }

    @Override
    public NewsWithComments viewNewsWithComments(int newsId, int commentPage, int commentSize) {
        Optional<News> optionalNews = newsRepository.findById(newsId);
        if (optionalNews.isPresent()) {
            News news = optionalNews.get();
            Pageable pageable = PageRequest.of(commentPage, commentSize);
            Page<Comment> commentsPage = commentRepository.findByIdNews(newsId, pageable);
            return new NewsWithComments(news, commentsPage.getContent());
        } else {
            throw new ResourceNotFoundException("No news with id = " + newsId + " was found");
        }
    }

    @Override
    public News updateNews(int newsId, News updatedNews) {
        if (newsRepository.existsById(newsId)) {
            updatedNews.setId(newsId); //устанавливаем переданный в url id обновленному пользователю
            newsRepository.save(updatedNews);
            log.debug("Update news with id = " + newsId + " with incoming params: " + updatedNews);
            return updatedNews;
        } else {
            throw new ResourceNotFoundException("Unable to update news. No news with id = " + newsId + " was found.");
        }
    }

    @Override
    public void deleteNews(int newsId) {
            newsRepository.deleteById(newsId);
            log.debug("News with id = " + newsId + " was successfully deleted (maybe).");
    }

    @Override
    public List<News> searchByTitleOrText(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository.searchByTitleOrText(keyword, pageable);
        log.debug("Find news by text (or title) = " + keyword + ". Founded news: " + newsPage);
        return newsPage.getContent();
    }
}
