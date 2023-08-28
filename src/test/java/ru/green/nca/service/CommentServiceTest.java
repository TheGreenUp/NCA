package ru.green.nca.service;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import ru.green.nca.dto.CommentDto;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.User;
import ru.green.nca.enums.UserRole;
import ru.green.nca.repository.CommentRepository;
import ru.green.nca.repository.NewsRepository;
import ru.green.nca.repository.UserRepository;
import ru.green.nca.security.UserDetailsImpl;
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
    @Mock
    private UserRepository userRepository;
    Comment COMMENT = new Comment(1, "Test comment", null, null, 1, 1);
    CommentDto COMMENT_DTO = new CommentDto(1, 1, "Test comment",
            null, null, "Даниил", "Гринь");
    User USER = new User(1, "TheGreenUp", "12345678", "Даниил",
            "Гринь", "Сергеевич", null, null, UserRole.ADMIN);
    @Test
    public void getByIdTest() {
        when(userRepository.findById(eq(1))).thenReturn(Optional.of(USER));
        when(commentRepository.findById(eq(1))).thenReturn(Optional.of(COMMENT));

        CommentDto commentById = this.commentService.getCommentById(1);

        assertNotNull(commentById);
        assertEquals(COMMENT.getId(), commentById.getId());
        assertEquals(COMMENT.getText(), commentById.getText());
    }

    @Test
    public void getAllCommentTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "creationDate"));
        Page<Comment> CommentPage = new PageImpl<>(List.of(COMMENT));

        when(userRepository.findById(eq(1))).thenReturn(Optional.of(USER));
        when(commentRepository.findAll(pageable)).thenReturn(CommentPage);

        List<CommentDto> comments = this.commentService.getComments(0, 10);

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals(COMMENT_DTO, comments.get(0));
    }

    @Test
    public void createCommentTest() {
        securityConfiguration();
        when(newsRepository.existsById(1)).thenReturn(true);
        when(commentRepository.save(eq(COMMENT))).thenReturn(COMMENT);
        when(userRepository.findById(eq(1))).thenReturn(Optional.of(USER));

        CommentDto comment = this.commentService.createComment(COMMENT_DTO);
        assertNotNull(comment);
        assertEquals(COMMENT.getId(), comment.getId());
        assertEquals(COMMENT.getText(), comment.getText());
    }

    @Test
    public void updateCommentTests() {
        securityConfiguration();
        when(commentRepository.findById(1)).thenReturn(Optional.ofNullable(COMMENT)); // Симулируем наличие новости с ID 1
        when(userRepository.findById(eq(1))).thenReturn(Optional.of(USER));
        when(commentRepository.save(COMMENT)).thenReturn(COMMENT);

        CommentDto updatedCommentDto = commentService.updateComment(1, COMMENT_DTO);

        assertNotNull(updatedCommentDto);
        assertEquals(COMMENT.getId(), updatedCommentDto.getId());
        assertEquals(COMMENT.getText(), updatedCommentDto.getText());
    }

    @Test
    public void deleteCommentTest() {
        securityConfiguration();
        when(commentRepository.findById(5)).thenReturn(Optional.ofNullable(COMMENT));
        doNothing().when(commentRepository).deleteById(5);

        commentService.deleteComment(5);
        verify(commentRepository, times(1)).deleteById(5);

    }

    private void securityConfiguration() {
        User testUser = USER;
        UserDetails userDetails = new UserDetailsImpl(testUser); // Создаем UserDetailsImpl для тестового пользователя

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication); // Устанавливаем аутентификацию для текущего теста
    }
}
