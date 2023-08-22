package ru.green.nca.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.green.nca.entity.Comment;
import ru.green.nca.exceptions.NoDataFoundException;
import ru.green.nca.exceptions.ResourceNotFoundException;
import ru.green.nca.repository.CommentRepository;
import ru.green.nca.service.CommentService;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    @Override
    public List<Comment> getComments(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100)); //устанавливаем максимальное ограничение страницы в 100 записей
        Page<Comment> commentPage = commentRepository.findAll(pageable);
        if (commentPage.isEmpty()) {
            log.warn("No news was found");
            throw new NoDataFoundException("No comments were found");
        }
        return commentPage.getContent();
    }
    @Override
    public Comment getCommentById(int commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id = " + commentId + "was not found"));

    }
    @Override
    public Comment createComment(Comment comment) {
        commentRepository.save(comment);
        log.debug("Create comments request with next params: " + comment);
        return comment;
    }
    @Override
    public void deleteComment(int commentId) {
            commentRepository.deleteById(commentId);
            log.debug("Comment with id = " + commentId + " was successfully deleted (maybe).");
    }
    @Override
    public Comment updateComment(int commentId, Comment updatedComment) {
        if(commentRepository.existsById(commentId)) {
            updatedComment.setId(commentId); //устанавливаем переданный в url id обновленному комментарию
            commentRepository.save(updatedComment);
            log.debug("Update comments with id = " + commentId + " with incoming params: " + updatedComment);
            return updatedComment;
        }
        else {
            throw new ResourceNotFoundException("Unable to update comment. No comment with id = " + commentId + " was found.");
        }

    }
}
