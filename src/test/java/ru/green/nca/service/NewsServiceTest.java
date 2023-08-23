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
import ru.green.nca.dto.NewsDto;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.News;
import ru.green.nca.dto.NewsWithCommentsDto;
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
    NewsDto NEWS_DTO = new NewsDto(1, "1984", "Orwell", null,
            null, null,null, null,null);
    @Test
    public void getByIdTest()  {
        when(newsRepository.findById(eq(5))).thenReturn(Optional.of(NEWS_1));
        News newsById = this.newsService.getNewsById(5);
        assertNotNull(newsById);
        assertEquals(NEWS_1.getId(), newsById.getId());
        assertEquals(NEWS_1.getTitle(), newsById.getTitle());
    }

    @Test
    public void getAllNewsTest()  {
        Pageable pageable = PageRequest.of(0, 10);
        Page<News> newsPage = new PageImpl<>(List.of(NEWS_1));
        when(newsRepository.findAll(pageable)).thenReturn(newsPage);

        List<News> news = this.newsService.getNews(0, 10);

        assertNotNull(news);
        assertEquals(1, news.size());
        assertEquals(NEWS_1, news.get(0));
    }

    @Test
    public void createNewsTest()  {
        when(newsRepository.save(any(News.class))).thenReturn(NEWS_1);
        News news = this.newsService.createNews(NEWS_DTO);
        assertNotNull(news);
        assertEquals(NEWS_1.getId(), news.getId());
        assertEquals(NEWS_1.getTitle(), news.getTitle());
    }

    @Test
    public void viewNewsWithCommentsTest()  {
        Comment comment = new Comment(10, "test text", null, null, 1, 1);
        NewsWithCommentsDto expectedNewsWithCommentsDto = new NewsWithCommentsDto(NEWS_1, List.of(comment));

        // Создаем моки
        Page<Comment> commentsPage = new PageImpl<>(List.of(comment));
        when(commentRepository.findByIdNews(eq(1), any(Pageable.class))).thenReturn(commentsPage);
        when(newsRepository.findById(eq(1))).thenReturn(Optional.ofNullable(NEWS_1));

        NewsWithCommentsDto resultNewsWithCommentsDto = newsService.viewNewsWithComments(1, 0, 10);

        assertNotNull(resultNewsWithCommentsDto);
        assertEquals(expectedNewsWithCommentsDto.getNews().getId(), resultNewsWithCommentsDto.getNews().getId());
        assertEquals(expectedNewsWithCommentsDto.getNews().getTitle(), resultNewsWithCommentsDto.getNews().getTitle());
        assertEquals(expectedNewsWithCommentsDto.getComments().size(), resultNewsWithCommentsDto.getComments().size());
    }

    @Test
    public void updateNewsTests()  {
        when(newsRepository.findById(1)).thenReturn(Optional.ofNullable(NEWS_1)); // Симулируем наличие новости с ID 1
        when(newsRepository.save(NEWS_1)).thenReturn(NEWS_1);

        News updatedNews = newsService.updateNews(1, NEWS_DTO);

        assertNotNull(updatedNews);
        assertEquals(NEWS_1.getId(), updatedNews.getId());
        assertEquals(NEWS_1.getTitle(), updatedNews.getTitle());
    }

    @Test
    public void deleteNewsTest()  {
        doNothing().when(newsRepository).deleteById(5);
        newsService.deleteNews(5);
        verify(newsRepository, times(1)).deleteById(5);
    }

    @Test
    public void searchByTitleOrTextTest()  {
        Pageable pageable = PageRequest.of(0, 10);
        Page<News> newsPage = new PageImpl<>(List.of(NEWS_1));
        when(newsRepository.searchByTitleOrText(eq("1984"), eq(pageable))).thenReturn(newsPage);
        List<News> news = this.newsService.searchByTitleOrText("1984", 0, 10);
        assertNotNull(news);
        assertEquals(newsPage.getContent(), news);
    }
}
