package ru.green.nca.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.green.nca.entity.User;
import ru.green.nca.enums.UserRole;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        User user = getUser();
        entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findByUsername("testUsername");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testUsername");
        assertThat(foundUser.get().getPassword()).isEqualTo("testPassword");
    }

    @Test
    public void testFindByUsernameNonExistent() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistentuser");

        assertThat(foundUser).isEmpty();
    }

    private User getUser() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setName("Игорёк");
        user.setSurname("Мегамозг");
        user.setParentName("Крушительвович");
        user.setRole(UserRole.JOURNALIST);
        return user;
    }
}
