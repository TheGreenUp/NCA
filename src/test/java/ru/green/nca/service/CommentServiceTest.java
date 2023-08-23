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
import ru.green.nca.repository.CommentRepository;
import ru.green.nca.repository.NewsRepository;
import ru.green.nca.service.impl.CommentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private NewsRepository newsRepository;
    Comment COMMENT = new Comment(1, "Test comment", null, null, 1, 1);

    @Test
    public void getByIdTest() {
        when(commentRepository.findById(eq(5))).thenReturn(Optional.of(COMMENT));
        Comment commentById = this.commentService.getCommentById(5);
        assertNotNull(commentById); // Проверка, что полученный объект не null
        assertEquals(COMMENT.getId(), commentById.getId()); // Проверка, что id совпадает
        assertEquals(COMMENT.getText(), commentById.getText()); // Проверка, что title совпадает
    }

    @Test
    public void getAllCommentTest() {
        Pageable pageable = PageRequest.of(0, 10); // Создаем объект Pageable для пагинации
        Page<Comment> CommentPage = new PageImpl<>(List.of(COMMENT)); // Создаем объект Page с данными
        when(commentRepository.findAll(pageable)).thenReturn(CommentPage); // Моделируем поведение репозитория

        List<Comment> Comment = this.commentService.getComments(0, 10);

        assertNotNull(Comment); // Проверка, что полученный объект не null
        assertEquals(1, Comment.size()); // Проверка, что список содержит 1 элемент
        assertEquals(COMMENT, Comment.get(0)); // Проверка, что элемент соответствует ожидаемому
    }

    @Test
    public void createCommentTest() {
        when(newsRepository.existsById(1)).thenReturn(true);
        when(commentRepository.save(eq(COMMENT))).thenReturn(COMMENT);
        Comment Comment = this.commentService.createComment(COMMENT);
        assertNotNull(Comment); // Проверка, что полученный объект не null
        assertEquals(COMMENT.getId(), Comment.getId()); // Проверка, что id совпадает
        assertEquals(COMMENT.getText(), Comment.getText()); // Проверка, что title совпадает
    }

    @Test
    public void updateCommentTests() {
        // Подготовьте мок repository
        when(commentRepository.findById(1)).thenReturn(Optional.ofNullable(COMMENT)); // Симулируем наличие новости с ID 1
        when(commentRepository.save(COMMENT)).thenReturn(COMMENT);

        // Вызываем метод обновления
        Comment updatedComment = commentService.updateComment(1, COMMENT);

        // Проверки
        assertNotNull(updatedComment);
        assertEquals(COMMENT.getId(), updatedComment.getId());
        assertEquals(COMMENT.getText(), updatedComment.getText());
        // Добавьте другие проверки, если необходимо
    }

    @Test
    public void deleteCommentTest() {
        // Mock the behavior of the repository's deleteById method
        doNothing().when(commentRepository).deleteById(5);
        // Call the service method to delete the Comment
        commentService.deleteComment(5);
        // Verify that the delete method was called with the correct ID
        verify(commentRepository, times(1)).deleteById(5);
    }
}
