package ru.green.nca.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.green.nca.dto.CommentDto;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.User;
import ru.green.nca.exceptions.ForbiddenException;
import ru.green.nca.exceptions.NoDataFoundException;
import ru.green.nca.exceptions.ResourceNotFoundException;
import ru.green.nca.repository.CommentRepository;
import ru.green.nca.repository.NewsRepository;
import ru.green.nca.security.UserDetailsImpl;
import ru.green.nca.service.CommentService;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private NewsRepository newsRepository;

    @Override
    public List<Comment> getComments(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        //TODO с этим тоже разобраться
        Page<Comment> commentPage = commentRepository.findAll(pageable);
        if (commentPage.isEmpty()) {
            log.warn("No comments were found");
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
    public Comment createComment(CommentDto commentDto) {
        Comment comment = convertToComment(commentDto);
        if (!newsRepository.existsById(comment.getIdNews()))
            throw new ResourceNotFoundException("News with id = " + comment.getIdNews() + " was not found");
        commentRepository.save(comment);
        log.debug("Create comments request with next params: " + comment);
        return comment;
    }

    @Override
    public void deleteComment(int commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id = " + commentId + " was not found"));
        if (getCurrentUser().getRoleId() != 1) {
            if (!Objects.equals(comment.getInsertedById(), getCurrentUser().getId()))
                throw new ForbiddenException("Access denied. You do not have permission to modify this comment.");
        }
        commentRepository.deleteById(commentId);
        log.debug("Comment with id = " + commentId + " was successfully deleted.");
    }

    @Override
    public Comment updateComment(int commentId, CommentDto updatedCommentDto) {
        Comment updatedComment = convertToComment(updatedCommentDto);
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id = " + commentId + " was not found"));
        BeanUtils.copyProperties(updatedComment, existingComment, getNullPropertyNames(updatedComment));
        if (!Objects.equals(existingComment.getInsertedById(), getCurrentUser().getId()))
            throw new ForbiddenException("Access denied. You do not have permission to modify this comment.");
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

    private Comment convertToComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setIdNews(commentDto.getIdNews());
        comment.setInsertedById(getCurrentUser().getId());
        return comment;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDetailsImpl userDetails1 = (UserDetailsImpl) userDetails;
        return userDetails1.getUser();
    }



}
