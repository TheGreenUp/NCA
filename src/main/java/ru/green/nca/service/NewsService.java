package ru.green.nca.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.green.nca.entity.News;
import ru.green.nca.repository.NewsRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class NewsService {
    private NewsRepository newsRepository;

    public ResponseEntity<News> getNewsById(int newsId) {
        try {
            Optional<News> news = newsRepository.findById(newsId);
            if (news.isEmpty()) throw new Exception("User not found");
            log.info("Find news with id = " + newsId + " request. " + news);
            return new ResponseEntity<>(news.orElse(null),HttpStatus.OK);
        } catch (Exception e) {//TODO кастомный нормальный exception
            log.info("Failed to find news with id = " + newsId +". News not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
