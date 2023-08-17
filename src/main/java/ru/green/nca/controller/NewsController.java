package ru.green.nca.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.entity.News;
import ru.green.nca.repository.NewsRepository;

import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/api/news")
public class NewsController {
    private NewsRepository newsRepository;
    private static Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    public NewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @GetMapping
    public ResponseEntity<List<News>> getNews() {
        List<News> news = newsRepository.findAll();
        logger.info("Find all news request");
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getUserById(@PathVariable("id") int newsId) {
        Optional<News> news = newsRepository.findById(newsId);
        logger.info("Find news by id = " + newsId + " request");
        return new ResponseEntity<>(news.orElse(null), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody News news) {
        newsRepository.save(news);
        logger.info("Create news request with next params: " + news.toString());
        return new ResponseEntity<>(news, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int newsId) {
        newsRepository.deleteById(newsId);
        logger.info("Delete news by id = " + newsId + " request");
        return new ResponseEntity<>("News successfully deleted", HttpStatus.OK);
    }
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<News> updateUser(@PathVariable("id") int newsId, @RequestBody News updatedNews) {
        updatedNews.setId(newsId);
        News news = newsRepository.save(updatedNews);
        logger.info("Update news with id = " + newsId + " with incoming params: " + updatedNews.toString());
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

}


