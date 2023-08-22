package ru.green.nca.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.entity.News;
import ru.green.nca.model.NewsWithComments;
import ru.green.nca.service.NewsService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/news")
@AllArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping
    public List<News> getNews(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        log.info("Entering 'get all news' endpoint");
        return newsService.getNews(page, size);
    }

    @GetMapping("/{id}")
    public News getNewsById(@PathVariable("id") int newsId) {
        log.info("Entering 'get news by id' endpoint");
        return newsService.getNewsById(newsId);
    }

    @PostMapping
    public News createNews(@RequestBody News news) {
        log.info("Entering 'create news' endpoint");
        return newsService.createNews(news);
    }

    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable("id") int newsId) {
        log.info("Entering 'delete news' endpoint");
        newsService.deleteNews(newsId);
    }

    @PutMapping("/{id}")
    public News updateNews(@PathVariable("id") int newsId, @RequestBody News updatedNews) {
        log.info("Entering 'update news' endpoint");
        return newsService.updateNews(newsId, updatedNews);
    }

    @GetMapping("/search")
    public Page<News> searchByTitleOrText(@RequestParam String keyword,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        log.info("Entering 'search news' endpoint");
        return newsService.searchByTitleOrText(keyword, page, size);
    }

    @GetMapping("/latest")
    public Page<News> getLatestNews(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Entering 'get latest news' endpoint");
        return newsService.getLatestNews(page, size);
    }

    @GetMapping("/{newsId}/comments")
    public NewsWithComments getNewsAndComments(
            @PathVariable int newsId,
            @RequestParam(defaultValue = "0") int commentPage,
            @RequestParam(defaultValue = "10") int commentSize) {
        log.info("Entering 'get news and comments' endpoint");
        return newsService.viewNewsWithComments(newsId, commentPage, commentSize);
    }
}



