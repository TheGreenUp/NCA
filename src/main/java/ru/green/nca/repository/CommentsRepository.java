package ru.green.nca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.green.nca.entity.Comments;
@Repository
public interface CommentsRepository extends JpaRepository<Comments,Integer> {
}
