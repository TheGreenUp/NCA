package ru.green.nca.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.green.nca.entity.News;
import ru.green.nca.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NewsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NewsRepository newsRepository;

    @Test
    public void searchByTitleOrTextTest() {
        News news_1 = new News().builder().title("Test title")
                .text("test text")
                .creationDate(Instant.now())
                .lastEditDate(Instant.now())
                .insertedById(1)
                .updatedById(1).build();
        News news_2 = new News().builder().title("some overwatch 2 news")
                .text("all bad")
                .creationDate(Instant.now())
                .lastEditDate(Instant.now())
                .insertedById(1)
                .updatedById(1).build();
        News news_3 = new News().builder().title("Pig pong")
                .text("SFunny some")
                .creationDate(Instant.now())
                .lastEditDate(Instant.now())
                .insertedById(1)
                .updatedById(1).build();

        Pageable pageable = PageRequest.of(0, 10);
        entityManager.persistAndFlush(news_1);
        entityManager.persistAndFlush(news_2);
        entityManager.persistAndFlush(news_3);

        List<News> foundNews = newsRepository.searchByTitleOrText("som", pageable).getContent();

        assertThat(foundNews).isNotNull();
        assertThat(foundNews.get(0).getText()).isEqualTo(news_2.getText());
        assertThat(foundNews.get(1).getText()).isEqualTo(news_3.getText());

    }
}