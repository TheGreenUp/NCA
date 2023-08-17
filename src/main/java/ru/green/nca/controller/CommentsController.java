package ru.green.nca.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.green.nca.entity.Comments;
import ru.green.nca.repository.CommentsRepository;

import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/api/comments")
public class CommentsController {
    private CommentsRepository commentsRepository;

    @Autowired
    public CommentsController(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    @GetMapping
    public List<Comments> getComments() {
        List<Comments> comments = commentsRepository.findAll();
        log.info("Find all comments request");
        return comments;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comments> getCommentById(@PathVariable("id") int commentId) {
        Optional<Comments> comment = commentsRepository.findById(commentId);
        log.info("Find comments by id = " + commentId + " request");
        return new ResponseEntity<>(comment.orElse(null), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Comments> createComments(@RequestBody Comments comment) {
        commentsRepository.save(comment);
        log.info("Create comments request with next params: " + comment.toString());
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") int commentId) {
        commentsRepository.deleteById(commentId);
        log.info("Delete comments by id = " + commentId + " request");
        return new ResponseEntity<>("comments successfully deleted", HttpStatus.OK);
    }
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<Comments> updateComment(@PathVariable("id") int commentsId, @RequestBody Comments updatedComment) {
        updatedComment.setId(commentsId);
        Comments comments = commentsRepository.save(updatedComment);
        log.info("Update comments with id = " + commentsId + " with incoming params: " + updatedComment.toString());
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }
}
