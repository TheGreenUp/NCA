package ru.green.nca.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.green.nca.dto.CommentDto;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.User;
import ru.green.nca.enums.UserRole;
import ru.green.nca.exceptions.ForbiddenException;
import ru.green.nca.exceptions.ResourceNotFoundException;
import ru.green.nca.repository.CommentRepository;
import ru.green.nca.repository.NewsRepository;
import ru.green.nca.security.UserDetailsImpl;
import ru.green.nca.service.CommentService;

import java.util.List;
import java.util.Objects;
/**
 * Реализация сервиса для работы с комментариями.
 */
@Service
@Slf4j
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private NewsRepository newsRepository;
    private ModelMapper modelMapper;

    /**
     * Получение списка комментариев с пагинацией и сортировкой по дате создания.
     *
     * @param page номер страницы (начиная с 0)
     * @param size количество комментариев на странице
     * @return список комментариев на заданной странице
     */
    @Override
    public List<Comment> getComments(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), //TODO захардкожена сотня, исправить
                Sort.by(Sort.Direction.DESC, "creationDate"));
        Page<Comment> commentPage = commentRepository.findAll(pageable);
        if (commentPage.isEmpty()) log.warn("No comments were found");
        return commentPage.getContent();

    }
    /**
     * Получение комментария по его идентификатору.
     *
     * @param commentId идентификатор комментария
     * @return объект комментария
     * @throws ResourceNotFoundException если комментарий не найден
     */
    @Override
    public Comment getCommentById(int commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id = " + commentId + " was not found"));
    }
    /**
     * Создание комментария на основе переданных данных.
     *
     * @param commentDto объект с данными для создания комментария
     * @return созданный комментарий
     * @throws ResourceNotFoundException если связанная новость не найдена
     */
    @Override
    public Comment createComment(CommentDto commentDto) {
        Comment comment = convertToComment(commentDto);
        if (!newsRepository.existsById(comment.getIdNews()))
            throw new ResourceNotFoundException("News with id = " + comment.getIdNews() + " was not found");
        log.debug("Create comments request with next params: " + comment);
        return commentRepository.save(comment);
    }
    /**
     * Удаление комментария по его идентификатору.
     *
     * @param commentId идентификатор комментария для удаления
     * @throws ResourceNotFoundException если комментарий не найден
     * @throws ForbiddenException если у текущего пользователя нет прав на удаление комментария
     */
    @Override
    public void deleteComment(int commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id = " + commentId + " was not found"));
        if (getCurrentUser().getRole() != UserRole.ADMIN) {//Если ты не админ, то проверяем дальше
            if (!Objects.equals(comment.getInsertedById(), getCurrentUser().getId()))
                throw new ForbiddenException("Access denied. You do not have permission to modify this comment.");
        }
        commentRepository.deleteById(commentId);
        log.debug("Comment with id = " + commentId + " was successfully deleted.");
    }
    /**
     * Обновление комментария на основе переданных данных.
     *
     * @param commentId идентификатор комментария для обновления
     * @param updatedCommentDto объект с обновленными данными комментария
     * @return обновленный комментарий
     * @throws ResourceNotFoundException если комментарий не найден
     * @throws ForbiddenException если у текущего пользователя нет прав на обновление комментария
     */
    @Override
    public Comment updateComment(int commentId, CommentDto updatedCommentDto) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id = " + commentId + " was not found"));

        existingComment.setText(updatedCommentDto.getText());
        if (!Objects.equals(existingComment.getInsertedById(), getCurrentUser().getId()))
            throw new ForbiddenException("Access denied. You do not have permission to modify this comment.");

        log.debug("Update comments with id = " + commentId + " with incoming params: " + updatedCommentDto);
        return commentRepository.save(existingComment);
    }
    /**
     * Конвертация из CommentDTO в Comment.
     *
     * @param commentDto DTO объект с данными
     * @return объект класса Comment
     */
    private Comment convertToComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setIdNews(commentDto.getIdNews());
        comment.setInsertedById(getCurrentUser().getId());
        return comment;
    }
    /**
     * Получение текущего пользователя.
     *
     * @return объект пользователя, представляющий текущего аутентифицированного пользователя
     */
    private User getCurrentUser() {
        // Получаем текущий объект аутентификации из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //получаем userDetails, связанные с аутентификацией
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDetailsImpl userData = (UserDetailsImpl) userDetails;
        return userData.getUser();

    }
}
