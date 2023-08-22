package ru.green.nca.service;

import ru.green.nca.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getComments(int page, int size);

    Comment getCommentById(int commentId);

    Comment createComments(Comment comment);

    void deleteComment(int commentId);

    Comment updateComment(int commentsId, Comment updatedComment);
}
