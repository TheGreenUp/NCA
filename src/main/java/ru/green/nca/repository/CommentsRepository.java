package ru.green.nca.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.green.nca.entity.Comments;
import ru.green.nca.entity.News;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments,Integer> {
    //List<Comments> findAllByIdNews(Integer newsId);
    Page<Comments> findByIdNews(Integer newsId, Pageable pageable);


}
