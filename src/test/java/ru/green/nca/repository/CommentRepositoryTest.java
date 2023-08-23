package ru.green.nca.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void findByNewsIdTest() {
        Comment comment = new Comment().builder()
                .text("Some funny text")
                .idNews(2)
                .insertedById(1)
                .creationDate(Instant.now())
                .lastEditDate(Instant.now()).build();

        Pageable pageable = PageRequest.of(0, 10);

        entityManager.persistAndFlush(comment);

        List<Comment> foundComments = commentRepository.findByIdNews(2, pageable).getContent();

        assertThat(foundComments).isNotNull();
        assertThat(foundComments.get(0).getText()).isEqualTo(comment.getText());

    }


}