package ru.green.nca.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.entity.News;
import ru.green.nca.model.NewsWithComments;
import ru.green.nca.service.NewsService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<List<News>> getNews(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return newsService.getNews(page, size);
    }

    @GetMapping("/{id}")
    public News getNewsById(@PathVariable("id") int newsId) {
        return newsService.getNewsById(newsId).getBody();
    }

    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody News news) {
        return newsService.createNews(news);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNews(@PathVariable("id") int newsId) {
        return newsService.deleteNews(newsId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable("id") int newsId, @RequestBody News updatedNews) {
        return newsService.updateNews(newsId, updatedNews);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<News>> searchByTitleOrText(@RequestParam String keyword,
                                                          @RequestParam int page,
                                                          @RequestParam int size) {
        return newsService.searchByTitleOrText(keyword, page, size);
    }

    @GetMapping("/latest")
    public ResponseEntity<Page<News>> getLatestNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return newsService.getLatestNews(page, size);
    }

    @GetMapping("/{newsId}/comments")
    public ResponseEntity<NewsWithComments> getNewsAndComments(
            @PathVariable int newsId,
            @RequestParam(defaultValue = "0") int commentPage,
            @RequestParam(defaultValue = "10") int commentSize) {
        return newsService.viewNewsWithComments(newsId, commentPage, commentSize);
    }
}



