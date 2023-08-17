package ru.green.nca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.green.nca.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
}
