package ru.green.nca.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.green.nca.entity.Comment;
import ru.green.nca.exceptions.NoDataFoundException;
import ru.green.nca.exceptions.ResourceNotFoundException;
import ru.green.nca.repository.CommentRepository;
import ru.green.nca.repository.NewsRepository;
import ru.green.nca.service.CommentService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private NewsRepository newsRepository;

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
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id = " + commentId + " was not found"));

    }

    @Override
    public Comment createComment(Comment comment) {
        if(!newsRepository.existsById(comment.getIdNews()))
            throw new ResourceNotFoundException("News with id = " + comment.getIdNews() + " was not found");
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
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id = " + commentId + " was not found"));
        BeanUtils.copyProperties(updatedComment, existingComment, getNullPropertyNames(updatedComment));
        log.debug("Update comments with id = " + commentId + " with incoming params: " + updatedComment);
        return commentRepository.save(existingComment);
    }

    public String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        // Получаем свойства объекта
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        // Создаем множество для хранения имен свойств, значение которых null
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            // Получаем значение свойства
            Object srcValue = src.getPropertyValue(pd.getName());

            // Если значение свойства равно null, добавляем имя свойства в множество
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
