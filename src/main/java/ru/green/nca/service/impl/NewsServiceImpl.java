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
import ru.green.nca.dto.NewsDto;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.News;
import ru.green.nca.exceptions.NoDataFoundException;
import ru.green.nca.exceptions.ResourceNotFoundException;
import ru.green.nca.dto.NewsWithCommentsDto;
import ru.green.nca.repository.CommentRepository;
import ru.green.nca.repository.NewsRepository;
import ru.green.nca.security.UserDetailsImpl;
import ru.green.nca.service.NewsService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {

    private NewsRepository newsRepository;
    private CommentRepository commentRepository;

    @Override
    public News getNewsById(int newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News with id = " + newsId + " was not found"));

    }

    @Override
    public List<News> getNews(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        Page<News> newsPage = newsRepository.findAll(pageable);
        if (newsPage.isEmpty()) {
            log.warn("No news was found");
            throw new NoDataFoundException("No news was found");
        }
        return newsPage.getContent();
    }

    @Override
    public News createNews(NewsDto newsDto) {
        News news = convertToNews(newsDto);
        setInsertedById(news);
        log.debug("Create news request with next params: " + newsDto);
        return newsRepository.save(news);
    }

    @Override
    public NewsWithCommentsDto viewNewsWithComments(int newsId, int commentPage, int commentSize) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("No news with id = " + newsId + " was found"));
        Pageable pageable = PageRequest.of(commentPage, commentSize);
        Page<Comment> commentsPage = commentRepository.findByIdNews(newsId, pageable);
        return new NewsWithCommentsDto(news, commentsPage.getContent());
    }

    @Override
    public News updateNews(int newsId, NewsDto updatedNewsDto) {
        News updatedNews = convertToNews(updatedNewsDto);
        setUpdatedUserId(updatedNews);
        News existingNews = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to update news. No news with id = " + newsId + " was found."));

        BeanUtils.copyProperties(updatedNews, existingNews, getNullPropertyNames(updatedNews));

        log.debug("Update news with id = " + newsId + " with incoming params: " + updatedNews);
        return newsRepository.save(existingNews);

    }

    @Override
    public void deleteNews(int newsId) {
        newsRepository.deleteById(newsId);
        log.debug("News with id = " + newsId + " was successfully deleted (maybe).");
    }

    @Override
    public List<News> searchByTitleOrText(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository.searchByTitleOrText(keyword, pageable);
        log.debug("Find news by text (or title) = " + keyword + ". Founded news: " + newsPage);
        return newsPage.getContent();
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

    private News convertToNews(NewsDto newsDto) {
        News news = new News();
        news.setId(newsDto.getId());
        news.setTitle(newsDto.getTitle());
        news.setText(newsDto.getText());
        return news;
    }

    private void setUpdatedUserId(News news){
        news.setUpdatedById(getCurrentUserId());
    }
    private void setInsertedById(News news){
        news.setInsertedById(getCurrentUserId());
    }
    private int getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return 1;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDetailsImpl userDetails1 = (UserDetailsImpl) userDetails;
        return userDetails1.getUser().getId();
    }

}
