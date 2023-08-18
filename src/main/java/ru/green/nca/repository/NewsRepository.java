package ru.green.nca.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.green.nca.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News,Integer> {
    @Query("SELECT n FROM News n WHERE n.title LIKE %:keyword% OR n.text LIKE %:keyword%")
    Page<News> searchByTitleOrText(String keyword, Pageable pageable);
    Page<News> findAllByOrderByCreationDateDesc(Pageable pageable);
    //TODO почему именно такие названия методов должны быть

}