package ru.green.nca;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.green.nca.entity.News;
import ru.green.nca.repository.NewsRepository;

import javax.enterprise.inject.New;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
class NcaApplicationTests {
	@Autowired
	private NewsRepository newsRepository;
	@Test
	void contextLoads() {
	}
}
