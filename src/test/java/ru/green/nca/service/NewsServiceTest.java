package ru.green.nca.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.green.nca.dto.CommentDto;
import ru.green.nca.dto.NewsDto;
import ru.green.nca.dto.NewsWithCommentsDto;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.News;
import ru.green.nca.entity.User;
import ru.green.nca.enums.UserRole;
import ru.green.nca.repository.CommentRepository;
import ru.green.nca.repository.NewsRepository;
import ru.green.nca.repository.UserRepository;
import ru.green.nca.security.UserDetailsImpl;
import ru.green.nca.service.impl.CommentServiceImpl;
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
    @Mock
    private CommentServiceImpl commentService;
    @Mock
    private UserRepository userRepository;

    News NEWS_1 = new News(1, "1984", "Orwell", null, null, 1, 1);
    NewsDto NEWS_DTO = new NewsDto(1, "1984", "Orwell", null,
            null, null, null, null, null);
    User USER = new User(1, "TheGreenUp", "encodedPassword", "Даниил",
            "Гринь", "Сергеевич", null, null, UserRole.ADMIN);

    @Test
    public void getByIdTest() {
        when(newsRepository.findById(eq(5))).thenReturn(Optional.of(NEWS_1));
        NewsDto newsById = this.newsService.getNewsById(5);
        assertNotNull(newsById);
        assertEquals(NEWS_1.getId(), newsById.getId());
        assertEquals(NEWS_1.getTitle(), newsById.getTitle());
    }

    @Test
    public void getAllNewsTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "creationDate"));
        Page<News> newsPage = new PageImpl<>(List.of(NEWS_1));
        when(newsRepository.findAll(pageable)).thenReturn(newsPage);

        List<NewsDto> news = this.newsService.getNews(0, 10);

        assertNotNull(news);
        assertEquals(1, news.size());
        assertEquals(NEWS_DTO, news.get(0));
    }

    @Test
    public void createNewsTest() {
        securityConfiguration();
        when(newsRepository.save(any(News.class))).thenReturn(NEWS_1);
        NewsDto news = this.newsService.createNews(NEWS_DTO);
        assertNotNull(news);
        assertEquals(NEWS_1.getId(), news.getId());
        assertEquals(NEWS_1.getTitle(), news.getTitle());
    }

    @Test
    public void viewNewsWithCommentsTest() {
        CommentDto commentDto = new CommentDto(10, 1, "test text", null, null, null, null);
        Comment comment = new Comment(1, "Test text", null, null, 1, 1);
        NewsWithCommentsDto expectedNewsWithCommentsDto = new NewsWithCommentsDto(NEWS_DTO, List.of(commentDto));

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
    public void updateNewsTests() {
        securityConfiguration();
        when(newsRepository.findById(1)).thenReturn(Optional.ofNullable(NEWS_1)); // Симулируем наличие новости с ID 1
        when(newsRepository.save(NEWS_1)).thenReturn(NEWS_1);

        NewsDto updatedNews = newsService.updateNews(1, NEWS_DTO);

        assertNotNull(updatedNews);
        assertEquals(NEWS_1.getId(), updatedNews.getId());
        assertEquals(NEWS_1.getTitle(), updatedNews.getTitle());
    }

    @Test
    public void deleteNewsTest() {
        doNothing().when(newsRepository).deleteById(5);
        newsService.deleteNews(5);
        verify(newsRepository, times(1)).deleteById(5);
    }

    @Test
    public void searchByTitleOrTextTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<News> newsPage = new PageImpl<>(List.of(NEWS_1));
        when(newsRepository.searchByTitleOrText(eq("Hanna"), eq(pageable))).thenReturn(newsPage);
        List<NewsDto> news = this.newsService.searchByTitleOrText("Hanna", 0, 10);
        assertNotNull(news);
        assertEquals(news, List.of(NEWS_DTO));
    }

    private void securityConfiguration() {
        User testUser = USER;
        UserDetails userDetails = new UserDetailsImpl(testUser); // Создаем UserDetailsImpl для тестового пользователя

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication); // Устанавливаем аутентификацию для текущего теста
    }
}
