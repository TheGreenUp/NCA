package ru.green.nca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.green.nca.entity.User;
public interface UserRepository extends JpaRepository<User,Integer> {
}
