package ru.green.nca.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.dto.CommentDto;
import ru.green.nca.entity.Comment;
import ru.green.nca.service.CommentService;

import java.util.List;

/**
 * Контроллер для обработки REST API запросов, связанных с сущностью "Comment".
 */
@Slf4j
@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * Получает список комментариев.
     *
     * @param page номер страницы (по умолчанию 0).
     * @param size количество комментариев на странице (по умолчанию 10).
     * @return Список комментариев.
     */
    @GetMapping
    public List<CommentDto> getComments(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        log.info("Entering 'get all comments' endpoint");
        return commentService.getComments(page, size);
    }
    /**
     * Получает комментарий по его идентификатору.
     *
     * @param commentId идентификатор комментария.
     * @return Комментарий с указанным идентификатором.
     */
    @GetMapping("/{id}")
    public CommentDto getCommentById(@PathVariable("id") int commentId) {
        log.info("Entering 'get comment by id' endpoint");
        return commentService.getCommentById(commentId);
    }

    /**
     * Создает новый комментарий.
     *
     * @param commentDto DTO с информацией о комментарии.
     * @return Созданный комментарий.
     */
    @PostMapping
    public CommentDto createComment(@RequestBody CommentDto commentDto) {
        log.info("Entering 'create comment' endpoint");
        return commentService.createComment(commentDto);
    }

    /**
     * Удаляет комментарий по его идентификатору.
     *
     * @param commentId идентификатор комментария.
     */
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable("id") int commentId) {
        log.info("Entering 'delete comment' endpoint");
        commentService.deleteComment(commentId);
    }

    /**
     * Обновляет информацию о комментарии.
     *
     * @param commentsId        идентификатор комментария.
     * @param updatedCommentDto DTO с обновленной информацией о комментарии.
     * @return Обновленный комментарий.
     */
    @PutMapping("/{id}")
    public CommentDto updateComment(@PathVariable("id") int commentsId, @RequestBody CommentDto updatedCommentDto) {
        log.info("Entering 'update comment' endpoint");
        updatedCommentDto.setId(commentsId);
        return commentService.updateComment(commentsId, updatedCommentDto);
    }
}
