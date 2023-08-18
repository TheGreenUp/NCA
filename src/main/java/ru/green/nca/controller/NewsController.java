package ru.green.nca.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.entity.News;
import ru.green.nca.model.NewsWithComments;
import ru.green.nca.repository.NewsRepository;
import ru.green.nca.service.NewsService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/news")
public class NewsController {
    private NewsRepository newsRepository;
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsRepository newsRepository, NewsService newsService) {
        this.newsRepository = newsRepository;
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<List<News>> getNews(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        //TODO добавить комменты про пагинацию
        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository.findAll(pageable);
        List<News> newsList = newsPage.getContent();
        return ResponseEntity.ok(newsList);
    }

    @GetMapping("/{id}")
    public News getNewsById(@PathVariable("id") int newsId) {
        return newsService.getNewsById(newsId).getBody();
    }

    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody News news) {
        //TODO в постмане дата выставляется как null null, но в Postgres все бомба
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

    @GetMapping("/search")
    public ResponseEntity<Page<News>> searchByTitleOrText(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository.searchByTitleOrText(keyword, pageable);
        log.info("Find news by text (or title) = " + keyword + ". Founded news: " + newsPage);
        return ResponseEntity.ok(newsPage);
    }

    @GetMapping("/latest")
    public ResponseEntity<Page<News>> getLatestNews(
            @RequestParam int page,
            @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        Page<News> newsPage = newsRepository.findAllByOrderByCreationDateDesc(pageable);
        log.info("Trying to find latest news");
        return ResponseEntity.ok(newsPage);
    }

    @GetMapping("/{newsId}/comments")
    public ResponseEntity<NewsWithComments> getNewsAndComments(
            @PathVariable int newsId,
            @RequestParam(defaultValue = "0") int commentPage,
            @RequestParam(defaultValue = "10") int commentSize) {
        return newsService.viewNewsWithComments(newsId, commentPage,commentSize);
    }
}



