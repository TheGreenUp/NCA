package ru.green.nca.service;

import ru.green.nca.dto.CommentDto;
import ru.green.nca.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getComments(int page, int size);

    Comment getCommentById(int commentId);

    Comment createComment(CommentDto comment);

    void deleteComment(int commentId);

    Comment updateComment(int commentsId, CommentDto updatedComment);
}
