package ru.green.nca.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.green.nca.repository.CommentsRepository;

@Service
@AllArgsConstructor
public class CommentService {
    private CommentsRepository commentsRepository;
}
