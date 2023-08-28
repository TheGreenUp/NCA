package ru.green.nca.service;

import ru.green.nca.dto.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getComments(int page, int size);

    CommentDto getCommentById(int commentId);

    CommentDto createComment(CommentDto comment);

    void deleteComment(int commentId);

    CommentDto updateComment(int commentsId, CommentDto updatedComment);
}
