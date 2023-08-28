package ru.green.nca;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import ru.green.nca.dto.NewsDto;
import ru.green.nca.entity.User;
import ru.green.nca.enums.UserRole;
import ru.green.nca.repository.NewsRepository;
import ru.green.nca.security.UserDetailsImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "test")
public class NcaApplicationTests {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    TestRestTemplate testRestTemplate;


    @Test
    void contextLoads() {
    }

    @Test
    public void testNewsFlow() {
        //TODO не получается, проблема с SecurityContextHolder
        securityConfiguration();
        ResponseEntity<NewsDto> newsDto = testRestTemplate.postForEntity("/api/news",
                new NewsDto(null, "1984", "Orwell", null,
                        null, "Артёмка", "Зеленый", null, null), NewsDto.class);
        System.out.println(newsDto);
    }

    private void securityConfiguration() {
        User testUser = new User(1, "TheGreenUp", "encodedPassword", "Даниил",
                "Гринь", "Сергеевич", null, null, UserRole.ADMIN);
        UserDetails userDetails = new UserDetailsImpl(testUser); // Создаем UserDetailsImpl для тестового пользователя
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication); // Устанавливаем аутентификацию для текущего теста
    }
}
