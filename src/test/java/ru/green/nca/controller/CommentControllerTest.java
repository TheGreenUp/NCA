package ru.green.nca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.green.nca.dto.CommentDto;
import ru.green.nca.entity.Comment;
import ru.green.nca.service.CommentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;
    Comment COMMENT = new Comment(1,"Test comment", null,null,1,1);
    CommentDto COMMENT_DTO = new CommentDto(1,1,"Test comment",
            null,null, null,null);
    @Test
    public void getAllCommentTest() throws Exception {
        when(commentService.getComments(eq(0), eq(10))).thenReturn(List.of(COMMENT));
        //сверху мы описываем поведение, что будет, если передадим 0 и 10 в качестве параметров
        //а снизу мы вызываем с этими параметрами, на что получаем результат из thenReturn()
        this.mockMvc.perform(get("/api/comments?page=0&size=10"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getCommentByIdTest() throws Exception {
        when(commentService.getCommentById(eq(3))).thenReturn(COMMENT);
        this.mockMvc.perform(get("/api/comments/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createCommentTest() throws Exception {
        when(commentService.createComment(COMMENT)).thenReturn(COMMENT);
        this.mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(COMMENT_DTO)))
                .andDo(print())
                .andExpect(status().isOk());
        //TODO чета ругается на content.contenttype
    }

    @Test
    public void deleteCommentTest() throws Exception {
        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isOk());
        verify(commentService).deleteComment(1);
    }

    @Test
    public void updateCommentTest() throws Exception {
        when(commentService.updateComment(eq(1), eq(COMMENT))).thenReturn(COMMENT);
        mockMvc.perform(put("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(COMMENT_DTO)))
                .andExpect(status().isOk());
        System.out.println(COMMENT);
        verify(commentService).updateComment(eq(1), eq(COMMENT));
    }
    // Метод для преобразования объекта в JSON строку
    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
