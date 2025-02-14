package ru.green.nca.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.green.nca.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findByIdNews(Integer newsId, Pageable pageable);
    List<Comment> findAllByInsertedById(Integer id);


}
