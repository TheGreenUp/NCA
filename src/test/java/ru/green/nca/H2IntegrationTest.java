package ru.green.nca;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.green.nca.entity.News;
import ru.green.nca.repository.NewsRepository;

import java.util.List;

@ActiveProfiles("test")
@DataJpaTest
@Slf4j
public class H2IntegrationTest {
    @Autowired
    private NewsRepository newsRepository;  // Замените на ваш репозиторий

    @Test
    public void testFindByTitle() {
        List<News> news = newsRepository.findAll();
        log.info("Вот вам данные емаё " + news.toString());
    }

}
