package ru.green.nca.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.dto.CommentDto;
import ru.green.nca.dto.UserDto;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.User;
import ru.green.nca.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @GetMapping
    public List<Comment> getComments(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        log.info("Entering 'get all comments' endpoint");
        return commentService.getComments(page,size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable("id") int commentId) {
        log.info("Entering 'get comment by id' endpoint");
        return new ResponseEntity<>(commentService.getCommentById(commentId), HttpStatus.OK);
    }

    @PostMapping
    public Comment createComment(@RequestBody CommentDto commentDto)
    {
        log.info("Entering 'create comment' endpoint");
        return commentService.createComment(convertToComment(commentDto));
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable("id") int commentId) {
        log.info("Entering 'delete comment' endpoint");
        commentService.deleteComment(commentId);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable("id") int commentsId, @RequestBody CommentDto updatedCommentDto) {
        log.info("Entering 'update comment' endpoint");
        updatedCommentDto.setId(commentsId);
        return commentService.updateComment(commentsId, convertToComment(updatedCommentDto));
        //TODO тута с конвертацией проблемы возникают
    }
    private Comment convertToComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setIdNews(commentDto.getIdNews());
        //TODO вот с этим тоже разобраться
        comment.setInsertedById(1);
        return comment;
    }
}
