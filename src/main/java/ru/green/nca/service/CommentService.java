package ru.green.nca.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.green.nca.entity.Comments;
import ru.green.nca.repository.CommentsRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CommentService {
    @Autowired
    private CommentsRepository commentsRepository;
    public List<Comments> getComments() {
        List<Comments> comments = commentsRepository.findAll();
        log.info("Find all comments request");
        return comments;
    }
    public ResponseEntity<Comments> getCommentById(@PathVariable("id") int commentId) {
        Optional<Comments> comment = commentsRepository.findById(commentId);
        log.info("Find comments by id = " + commentId + " request");
        return new ResponseEntity<>(comment.orElse(null), HttpStatus.OK);
    }
    public ResponseEntity<Comments> createComments(@RequestBody Comments comment) {
        commentsRepository.save(comment);
        log.info("Create comments request with next params: " + comment.toString());
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }
    public ResponseEntity<String> deleteComment(@PathVariable("id") int commentId) {
        commentsRepository.deleteById(commentId);
        log.info("Delete comments by id = " + commentId + " request");
        return new ResponseEntity<>("comments successfully deleted", HttpStatus.OK);
    }
    public ResponseEntity<Comments> updateComment(@PathVariable("id") int commentsId, @RequestBody Comments updatedComment) {
        updatedComment.setId(commentsId);
        Comments comments = commentsRepository.save(updatedComment);
        log.info("Update comments with id = " + commentsId + " with incoming params: " + updatedComment.toString());
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }
}
