package ru.green.nca.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.green.nca.dto.CommentDto;
import ru.green.nca.dto.NewsDto;
import ru.green.nca.dto.NewsWithCommentsDto;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.News;
import ru.green.nca.entity.User;
import ru.green.nca.enums.UserRole;
import ru.green.nca.exceptions.ForbiddenException;
import ru.green.nca.exceptions.ResourceNotFoundException;
import ru.green.nca.repository.CommentRepository;
import ru.green.nca.repository.NewsRepository;
import ru.green.nca.repository.UserRepository;
import ru.green.nca.service.NewsService;
import ru.green.nca.util.CurrentUserProvider;

import java.util.*;
import java.util.function.Consumer;

/**
 * Реализация сервиса для работы с новостями.
 */
@Slf4j
@Service
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {

    private NewsRepository newsRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;

    private CommentServiceImpl commentService;

    /**
     * Получение новости по её идентификатору.
     *
     * @param newsId идентификатор новости
     * @return найденная новость
     * @throws ResourceNotFoundException если новость не найдена
     */
    @Override
    public NewsDto getNewsById(int newsId) {
        return convertToNewsDto(newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News with id = " + newsId + " was not found")));


    }

    /**
     * Получение списка новостей с пагинацией и сортировкой по дате создания.
     *
     * @param page номер страницы (начиная с 0)
     * @param size количество новостей на странице
     * @return список новостей на заданной странице
     */
    @Override
    public List<NewsDto> getNews(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(Sort.Direction.DESC, "creationDate"));
        Page<News> newsPage = newsRepository.findAll(pageable);
        if (newsPage.isEmpty()) {
            log.warn("No news was found");
        }
        return newsPage.getContent().stream()
                .map(this::convertToNewsDto)
                .toList();
    }

    /**
     * Создание новости на основе переданных данных.
     *
     * @param newsDto объект с данными для создания новости
     * @return созданная новость
     */
    @Override
    public NewsDto createNews(NewsDto newsDto) {
        News news = convertToNews(newsDto);
        news.setInsertedById(CurrentUserProvider.getInstance().getCurrentUser().getId());
        log.debug("Create news request with next params: " + newsDto);
        return convertToNewsDto(newsRepository.save(news));
    }

    /**
     * Получение новости и комментариев к ней по идентификатору новости.
     *
     * @param newsId      идентификатор новости
     * @param commentPage номер страницы комментариев (начиная с 0)
     * @param commentSize количество комментариев на странице
     * @return объект NewsWithCommentsDto, содержащий новость и список комментариев на заданной странице
     * @throws ResourceNotFoundException если новость не найдена
     */
    @Override
    public NewsWithCommentsDto viewNewsWithComments(int newsId, int commentPage, int commentSize) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("No news with id = " + newsId + " was found"));
        Pageable pageable = PageRequest.of(commentPage, commentSize);
        Page<Comment> commentsPage = commentRepository.findByIdNews(newsId, pageable);
        List<CommentDto> commentDtos = commentsPage.getContent().stream()
                .map(comment -> commentService.convertToCommentDto(comment))
                .toList();
        return new NewsWithCommentsDto(convertToNewsDto(news), commentDtos);
    }

    /**
     * Обновление новости на основе переданных данных.
     *
     * @param newsId         идентификатор новости для обновления
     * @param updatedNewsDto объект с обновленными данными новости
     * @return обновленная новость
     * @throws ResourceNotFoundException если новость для обновления не найдена
     */
    @Override
    public NewsDto updateNews(int newsId, NewsDto updatedNewsDto) {
        News existingNews = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to update news. No news with id = " + newsId + " was found."));
        if (CurrentUserProvider.getInstance().getCurrentUser().getRole() != UserRole.ADMIN) {
            if (!Objects.equals(existingNews.getInsertedById(), CurrentUserProvider.getInstance().getCurrentUser().getId()))
                throw new ForbiddenException("Access denied. You do not have permission to modify this comment.");
        }

        News updatedNews = convertToNews(updatedNewsDto);
        updatedNews.setUpdatedById(CurrentUserProvider.getInstance().getCurrentUser().getId());
        BeanUtils.copyProperties(updatedNews, existingNews, getNullPropertyNames(updatedNews));


        log.debug("Update news with id = " + newsId + " with incoming params: " + updatedNews);
        return convertToNewsDto(newsRepository.save(existingNews));

    }

    /**
     * Удаление новости по идентификатору.
     *
     * @param newsId идентификатор новости для удаления
     * @throws ResourceNotFoundException если новость для удаления не найдена
     */
    @Override
    public void deleteNews(int newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id = " + newsId + " was not found"));

        if (CurrentUserProvider.getInstance().getCurrentUser().getRole() != UserRole.ADMIN) {//Если ты не админ, то проверяем дальше
            if (!Objects.equals(news.getInsertedById(), CurrentUserProvider.getInstance().getCurrentUser().getId()))
                throw new ForbiddenException("Access denied. You do not have permission to modify this comment.");
        }
        newsRepository.deleteById(newsId);
        log.debug("News with id = " + newsId + " was successfully deleted.");
    }

    /**
     * Поиск новостей по ключевому слову (по тексту или заголовку) с пагинацией.
     *
     * @param keyword ключевое слово для поиска
     * @param page    номер страницы (начиная с 0)
     * @param size    количество новостей на странице
     * @return список найденных новостей на заданной странице
     */
    @Override
    public List<NewsDto> searchByTitleOrText(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository.searchByTitleOrText(keyword, pageable);
        log.debug("Find news by text (or title) = " + keyword + ". Founded news: " + newsPage);
        return newsPage.getContent().stream()
                .map(this::convertToNewsDto)
                .toList();
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

    /**
     * Конвертация объекта NewsDto в сущность News.
     *
     * @param newsDto объект NewsDto для конвертации
     * @return сущность News
     */
    private News convertToNews(NewsDto newsDto) {
        News news = new News();
        news.setId(newsDto.getId());
        news.setTitle(newsDto.getTitle());
        news.setText(newsDto.getText());
        return news;
    }

    /**
     * Конвертация объекта News в сущность NewsDto.
     *
     * @param news объект News для конвертации
     * @return сущность NewsDto
     */
    private NewsDto convertToNewsDto(News news) {
        NewsDto newsDto = new NewsDto();
        newsDto.setId(news.getId());
        newsDto.setTitle(news.getTitle());
        newsDto.setText(news.getText());
        newsDto.setCreationDate(news.getCreationDate());
        newsDto.setLastEditDate(news.getLastEditDate());

        setUserInformation(news.getInsertedById(), newsDto::setAuthorName, newsDto::setAuthorSurname);
        setUserInformation(news.getUpdatedById(), newsDto::setUpdatedByName, newsDto::setUpdatedBySurname);

        return newsDto;
    }

    /**
     * Установка информации о пользователе (имя и фамилия) на основе его ID.
     *
     * @param userId        ID пользователя
     * @param nameSetter    Консьюмер для установки имени пользователя
     * @param surnameSetter Консьюмер для установки фамилии пользователя
     */
    private void setUserInformation(Integer userId, Consumer<String> nameSetter, Consumer<String> surnameSetter) {
        if (userId != null) {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                nameSetter.accept(user.getName());
                surnameSetter.accept(user.getSurname());
            }
        } else {
            nameSetter.accept("UNKNOWN");
            surnameSetter.accept("UNKNOWN");
        }
    }
}
