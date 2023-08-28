package ru.green.nca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.green.nca.dto.CommentDto;
import ru.green.nca.dto.NewsDto;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.News;
import ru.green.nca.dto.NewsWithCommentsDto;
import ru.green.nca.security.JWTUtil;
import ru.green.nca.service.NewsService;
import ru.green.nca.util.JsonConverter;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NewsController.class)
@AutoConfigureMockMvc(addFilters = false)

public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @MockBean
    private JWTUtil jwtUtil;
    News NEWS_1 = new News(1, "1984", "Orwell", null, null, 1, 1);
    NewsDto NEWS_DTO = new NewsDto(1, "1984", "Orwell", null,
            null, null,null, null,null);
    CommentDto commentDto = new CommentDto(10, 1,"test text",null,null,null,null);
    Comment comment = new Comment(1,"Test text",null,null,1,1);

    @Test
    public void getAllNewsTest() throws Exception {
        when(newsService.getNews(eq(0), eq(10))).thenReturn(List.of(NEWS_DTO));
        //сверху мы описываем поведение, что будет, если передадим 0 и 10 в качестве параметров
        //а снизу мы вызываем с этими параметрами, на что получаем результат из thenReturn()
        this.mockMvc.perform(get("/api/news?page=0&size=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[" + JsonConverter.asJsonString(NEWS_DTO) + "]"));
    }

    @Test
    public void getNewsByIdTest() throws Exception {
        when(newsService.getNewsById(eq(3))).thenReturn(NEWS_DTO);
        this.mockMvc.perform(get("/api/news/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(JsonConverter.asJsonString(NEWS_DTO)));
    }

    @Test
    public void createNewsTest() throws Exception {
        when(newsService.createNews(NEWS_DTO)).thenReturn(NEWS_DTO);
        this.mockMvc.perform(post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConverter.asJsonString(NEWS_1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(JsonConverter.asJsonString(NEWS_DTO)));
   }

    @Test
    public void deleteNewsTest() throws Exception {
        mockMvc.perform(delete("/api/news/1"))
                .andExpect(status().isOk());
        verify(newsService).deleteNews(1);
    }

    @Test
    public void updateNewsTest() throws Exception {
        when(newsService.updateNews(eq(1), eq(NEWS_DTO))).thenReturn(NEWS_DTO);

        mockMvc.perform(put("/api/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConverter.asJsonString(NEWS_DTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(JsonConverter.asJsonString(NEWS_DTO)));

        verify(newsService).updateNews(eq(1), eq(NEWS_DTO));
    }

    @Test
    public void searchNewsTest() throws Exception {
        when(newsService.searchByTitleOrText(eq("1984"), eq(0), eq(10))).thenReturn(List.of(NEWS_DTO));
        this.mockMvc.perform(get("/api/news/search?keyword=1984&page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConverter.asJsonString(NEWS_1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[" +JsonConverter.asJsonString(NEWS_DTO) + "]"));

    }
    @Test
    public void findNewsWithCommentsTest() throws Exception {
        NewsWithCommentsDto news = new NewsWithCommentsDto(NEWS_DTO, List.of(commentDto));
        JsonConverter.asJsonString(news);
        when(newsService.viewNewsWithComments(eq(11),eq(0),eq(10))).thenReturn(news);
        this.mockMvc.perform(get("/api/news/11/comments?page=0&size=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(JsonConverter.asJsonString(news)));
    }
}
