package ru.green.nca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.green.nca.entity.News;

public interface NewsRepository extends JpaRepository<News,Integer> {
}
