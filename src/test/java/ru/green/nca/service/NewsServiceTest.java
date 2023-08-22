package ru.green.nca.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.News;
import ru.green.nca.model.NewsWithComments;
import ru.green.nca.repository.CommentRepository;
import ru.green.nca.repository.NewsRepository;
import ru.green.nca.service.impl.NewsServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {
    @InjectMocks
    private NewsServiceImpl newsService;
    @Mock
    private NewsRepository newsRepository;
    @Mock
    private CommentRepository commentRepository;
    News NEWS_1 = new News(5, "1984", "Orwell", null, null, 1, 1);

    @Test
    public void getByIdTest()  {
        when(newsRepository.findById(eq(5))).thenReturn(Optional.of(NEWS_1));
        News newsById = this.newsService.getNewsById(5);
        assertNotNull(newsById); // Проверка, что полученный объект не null
        assertEquals(NEWS_1.getId(), newsById.getId()); // Проверка, что id совпадает
        assertEquals(NEWS_1.getTitle(), newsById.getTitle()); // Проверка, что title совпадает
    }

    @Test
    public void getAllNewsTest()  {
        Pageable pageable = PageRequest.of(0, 10); // Создаем объект Pageable для пагинации
        Page<News> newsPage = new PageImpl<>(List.of(NEWS_1)); // Создаем объект Page с данными
        when(newsRepository.findAll(pageable)).thenReturn(newsPage); // Моделируем поведение репозитория

        List<News> news = this.newsService.getNews(0, 10);

        assertNotNull(news); // Проверка, что полученный объект не null
        assertEquals(1, news.size()); // Проверка, что список содержит 1 элемент
        assertEquals(NEWS_1, news.get(0)); // Проверка, что элемент соответствует ожидаемому
    }

    @Test
    public void createNewsTest()  {
        when(newsRepository.save(eq(NEWS_1))).thenReturn(NEWS_1);
        News news = this.newsService.createNews(NEWS_1);
        assertNotNull(news); // Проверка, что полученный объект не null
        assertEquals(NEWS_1.getId(), news.getId()); // Проверка, что id совпадает
        assertEquals(NEWS_1.getTitle(), news.getTitle()); // Проверка, что title совпадает
    }

    @Test
    public void viewNewsWithCommentsTest()  {
        Comment comment = new Comment(10, "test text", null, null, 1, 1);
        NewsWithComments expectedNewsWithComments = new NewsWithComments(NEWS_1, List.of(comment));

        // Создаем моки
        Page<Comment> commentsPage = new PageImpl<>(List.of(comment));
        when(commentRepository.findByIdNews(eq(1), any(Pageable.class))).thenReturn(commentsPage);
        when(newsRepository.findById(eq(1))).thenReturn(Optional.ofNullable(NEWS_1));

        // Вызываем метод
        NewsWithComments resultNewsWithComments = newsService.viewNewsWithComments(1, 0, 10);

        // Проверяем, что результат соответствует ожиданиям
        assertNotNull(resultNewsWithComments);
        assertEquals(expectedNewsWithComments.getNews().getId(), resultNewsWithComments.getNews().getId());
        assertEquals(expectedNewsWithComments.getNews().getTitle(), resultNewsWithComments.getNews().getTitle());
        assertEquals(expectedNewsWithComments.getComments().size(), resultNewsWithComments.getComments().size());
        // Добавьте другие проверки, если необходимо
    }

    @Test
    public void updateNewsTests()  {
        // Подготовьте мок repository
        when(newsRepository.existsById(1)).thenReturn(true); // Симулируем наличие новости с ID 1
        when(newsRepository.save(NEWS_1)).thenReturn(NEWS_1);

        // Вызываем метод обновления
        News updatedNews = newsService.updateNews(1, NEWS_1);

        // Проверки
        assertNotNull(updatedNews);
        assertEquals(NEWS_1.getId(), updatedNews.getId());
        assertEquals(NEWS_1.getTitle(), updatedNews.getTitle());
        // Добавьте другие проверки, если необходимо
    }

    @Test
    public void deleteNewsTest()  {
        // Mock the behavior of the repository's deleteById method
        doNothing().when(newsRepository).deleteById(5);
        // Call the service method to delete the news
        newsService.deleteNews(5);
        // Verify that the delete method was called with the correct ID
        verify(newsRepository, times(1)).deleteById(5);
    }

    @Test
    public void searchByTitleOrTextTest()  {
        Pageable pageable = PageRequest.of(0, 10); // Создаем объект Pageable для пагинации
        Page<News> newsPage = new PageImpl<>(List.of(NEWS_1)); // Создаем объект Page с данными
        when(newsRepository.searchByTitleOrText(eq("1984"), eq(pageable))).thenReturn(newsPage);
        List<News> news = this.newsService.searchByTitleOrText("1984", 0, 10);
        assertNotNull(news);
        assertEquals(newsPage.getContent(), news);
    }
}
