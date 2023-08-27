package ru.green.nca.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.dto.NewsDto;
import ru.green.nca.entity.News;
import ru.green.nca.dto.NewsWithCommentsDto;
import ru.green.nca.service.NewsService;

import java.util.List;
/**
 * Контроллер для обработки REST API запросов, связанных с сущностью "News".
 */
@Slf4j
@RestController
@RequestMapping("/api/news")
@AllArgsConstructor
public class NewsController {
    private final NewsService newsService;
    /**
     * Получение списка новостей с пагинацией.
     *
     * @param page номер страницы (по умолчанию: 0)
     * @param size количество новостей на странице (по умолчанию: 10)
     * @return список новостей
     */
    @GetMapping
    public List<News> getNews(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        log.info("Entering 'get all news' endpoint");
        return newsService.getNews(page, size);
    }
    /**
     * Получение новости по её идентификатору.
     *
     * @param newsId идентификатор новости
     * @return найденная новость
     */
    @GetMapping("/{id}")
    public News getNewsById(@PathVariable("id") int newsId) {
        log.info("Entering 'get news by id' endpoint");
        return newsService.getNewsById(newsId);
    }
    /**
     * Создание новой новости.
     *
     * @param newsDto данные новости для создания
     * @return созданная новость
     */
    @PostMapping
    public News createNews(@RequestBody NewsDto newsDto) {
        log.info("Entering 'create news' endpoint");
        return newsService.createNews(newsDto);
    }
    /**
     * Удаление новости по идентификатору.
     *
     * @param newsId идентификатор новости для удаления
     */
    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable("id") int newsId) {
        log.info("Entering 'delete news' endpoint");
        newsService.deleteNews(newsId);
    }
    /**
     * Обновление информации о новости.
     *
     * @param newsId           идентификатор новости для обновления
     * @param updatedNewsDto   обновленные данные новости
     * @return обновленная новость
     */
    @PutMapping("/{id}")
    public News updateNews(@PathVariable("id") int newsId, @RequestBody NewsDto updatedNewsDto) {
        log.info("Entering 'update news' endpoint");
        updatedNewsDto.setId(newsId);
        return newsService.updateNews(newsId, updatedNewsDto);
    }
    /**
     * Поиск новостей по ключевым словам в заголовке или тексте с пагинацией.
     *
     * @param keyword ключевое слово для поиска
     * @param page    номер страницы (по умолчанию: 0)
     * @param size    количество новостей на странице (по умолчанию: 10)
     * @return список найденных новостей
     */
    @GetMapping("/search")
    public List<News> searchByTitleOrText(@RequestParam String keyword,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        log.info("Entering 'search news' endpoint");
        return newsService.searchByTitleOrText(keyword, page, size);
    }
    /**
     * Получение новости и её комментариев с пагинацией.
     *
     * @param newsId      идентификатор новости
     * @param commentPage номер страницы комментариев (по умолчанию: 0)
     * @param commentSize количество комментариев на странице (по умолчанию: 10)
     * @return новость и список её комментариев
     */
    @GetMapping("/{newsId}/comments")
    public NewsWithCommentsDto getNewsAndComments(
            @PathVariable int newsId,
            @RequestParam(defaultValue = "0") int commentPage,
            @RequestParam(defaultValue = "10") int commentSize) {
        log.info("Entering 'get news and comments' endpoint");
        return newsService.viewNewsWithComments(newsId, commentPage, commentSize);
    }
}



