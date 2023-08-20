package ru.green.nca.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.green.nca.entity.Comments;
import ru.green.nca.entity.News;
import ru.green.nca.model.NewsWithComments;
import ru.green.nca.repository.CommentsRepository;
import ru.green.nca.repository.NewsRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private CommentsRepository commentsRepository;

    public ResponseEntity<News> getNewsById(int newsId) {
        try {
            Optional<News> news = newsRepository.findById(newsId);
            if (news.isEmpty()) throw new Exception("User not found");
            log.info("Find news with id = " + newsId + " request. " + news);
            return new ResponseEntity<>(news.orElse(null), HttpStatus.OK);
        } catch (Exception e) {//TODO кастомный нормальный exception
            log.info("Failed to find news with id = " + newsId + ". News not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<News>> getNews(int page, int size){
        //TODO добавить комменты про пагинацию
        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository.findAll(pageable);
        List<News> newsList = newsPage.getContent();
        return ResponseEntity.ok(newsList);
    }
    public ResponseEntity<News> createNews( News news) {
        //TODO в постмане дата выставляется как null null, но в Postgres все бомба
        newsRepository.save(news);
        log.info("Create news request with next params: " + news.toString());
        return new ResponseEntity<>(news, HttpStatus.CREATED);
    }
    public ResponseEntity<NewsWithComments> viewNewsWithComments(int newsId, int commentPage, int commentSize) {
        Optional<News> optionalNews = newsRepository.findById(newsId);

        if (optionalNews.isPresent()) {
            News news = optionalNews.get();

            Pageable pageable = PageRequest.of(commentPage, commentSize);
            Page<Comments> commentsPage = commentsRepository.findByIdNews(newsId, pageable);
            //TODO шото пейджы не работают
            NewsWithComments newsWithComments = new NewsWithComments(news, commentsPage.getContent());

            return ResponseEntity.ok(newsWithComments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<News> updateNews( int newsId, News updatedNews) {
        updatedNews.setId(newsId);
        News news = newsRepository.save(updatedNews);
        log.info("Update news with id = " + newsId + " with incoming params: " + updatedNews.toString());
        return new ResponseEntity<>(news, HttpStatus.OK);
    }
    public ResponseEntity<String> deleteNews(int newsId) {
        newsRepository.deleteById(newsId);
        log.info("Delete news by id = " + newsId + " request");
        return new ResponseEntity<>("News successfully deleted", HttpStatus.OK);
    }
    public ResponseEntity<Page<News>> searchByTitleOrText( String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository.searchByTitleOrText(keyword, pageable);
        log.info("Find news by text (or title) = " + keyword + ". Founded news: " + newsPage);
        return ResponseEntity.ok(newsPage);
    }
    public ResponseEntity<Page<News>> getLatestNews(int page,int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        Page<News> newsPage = newsRepository.findAllByOrderByCreationDateDesc(pageable);
        log.info("Trying to find latest news");
        return ResponseEntity.ok(newsPage);
    }
}
