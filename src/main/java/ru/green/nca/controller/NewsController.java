package ru.green.nca.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.dto.NewsDto;
import ru.green.nca.entity.News;
import ru.green.nca.dto.NewsWithCommentsDto;
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
    public News createNews(@RequestBody NewsDto newsDto) {
        log.info("Entering 'create news' endpoint");
        return newsService.createNews(convertToNews(newsDto));
    }

    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable("id") int newsId) {
        log.info("Entering 'delete news' endpoint");
        newsService.deleteNews(newsId);
    }

    @PutMapping("/{id}")
    public News updateNews(@PathVariable("id") int newsId, @RequestBody NewsDto updatedNewsDto) {
        log.info("Entering 'update news' endpoint");
        updatedNewsDto.setId(newsId);
        return newsService.updateNews(newsId, convertToNews(updatedNewsDto));
    }

    @GetMapping("/search")
    public List<News> searchByTitleOrText(@RequestParam String keyword,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        log.info("Entering 'search news' endpoint");
        return newsService.searchByTitleOrText(keyword, page, size);
    }

    @GetMapping("/{newsId}/comments")
    public NewsWithCommentsDto getNewsAndComments(
            @PathVariable int newsId,
            @RequestParam(defaultValue = "0") int commentPage,
            @RequestParam(defaultValue = "10") int commentSize) {
        log.info("Entering 'get news and comments' endpoint");
        return newsService.viewNewsWithComments(newsId, commentPage, commentSize);
    }

    private News convertToNews(NewsDto newsDto) {
        News news = new News();
        news.setId(newsDto.getId());
        news.setTitle(newsDto.getTitle());
        news.setText(newsDto.getText());
        //TODO необходимо через spring security получать данные и вставлять сюда
        news.setInsertedById(1);
        news.setUpdatedById(1);
        return news;
    }
}



