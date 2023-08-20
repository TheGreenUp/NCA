package ru.green.nca.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.entity.Comments;
import ru.green.nca.service.CommentService;
import ru.green.nca.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/comments")
public class CommentsController {
    private final CommentService commentService;

    @Autowired
    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<Comments> getComments() {
    return commentService.getComments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comments> getCommentById(@PathVariable("id") int commentId) {
        return commentService.getCommentById(commentId);
    }

    @PostMapping
    public ResponseEntity<Comments> createComments(@RequestBody Comments comment) {
        return commentService.createComments(comment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") int commentId) {
        return commentService.deleteComment(commentId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comments> updateComment(@PathVariable("id") int commentsId, @RequestBody Comments updatedComment) {
        return commentService.updateComment(commentsId,updatedComment);
    }
}
