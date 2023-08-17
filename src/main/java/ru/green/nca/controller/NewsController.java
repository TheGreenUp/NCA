package ru.green.nca.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.entity.News;
import ru.green.nca.repository.NewsRepository;
import ru.green.nca.service.NewsService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final NewsRepository newsRepository;
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsRepository newsRepository, NewsService newsService) {
        this.newsRepository = newsRepository;
        this.newsService = newsService;
    }

    @GetMapping
    public List<News> getNews() {
        List<News> news = newsRepository.findAll();
        log.info("Find all news request");
        return news;
    }

    @GetMapping("/{id}")
    public News getNewsById(@PathVariable("id") int newsId) {
        return newsService.getNewsById(newsId).getBody();
    }

    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody News news) {
        newsRepository.save(news);
        log.info("Create news request with next params: " + news.toString());
        return new ResponseEntity<>(news, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNews(@PathVariable("id") int newsId) {
        newsRepository.deleteById(newsId);
        log.info("Delete news by id = " + newsId + " request");
        return new ResponseEntity<>("News successfully deleted", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable("id") int newsId, @RequestBody News updatedNews) {
        updatedNews.setId(newsId);
        News news = newsRepository.save(updatedNews);
        log.info("Update news with id = " + newsId + " with incoming params: " + updatedNews.toString());
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

}


