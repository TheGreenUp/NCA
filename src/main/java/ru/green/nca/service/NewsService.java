package ru.green.nca.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
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
}
