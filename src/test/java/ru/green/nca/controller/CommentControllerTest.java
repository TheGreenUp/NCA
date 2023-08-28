package ru.green.nca.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.green.nca.dto.CommentDto;
import ru.green.nca.entity.Comment;
import ru.green.nca.security.JWTUtil;
import ru.green.nca.service.CommentService;
import ru.green.nca.util.JsonConverter;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommentService commentService;
    @MockBean
    private JWTUtil jwtUtil;

    Comment COMMENT = new Comment(1, "Test comment", null, null, 1, 1);
    CommentDto COMMENT_DTO = new CommentDto(1, 1, "Test comment",
            null, null, null, null);

    @Test
    public void getAllCommentTest() throws Exception {
        when(commentService.getComments(eq(0), eq(10))).thenReturn(List.of(COMMENT_DTO));
        //сверху мы описываем поведение, что будет, если передадим 0 и 10 в качестве параметров
        //а снизу мы вызываем с этими параметрами, на что получаем результат из thenReturn()
        this.mockMvc.perform(get("/api/comments?page=0&size=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[" + JsonConverter.asJsonString(COMMENT_DTO) + "]"));
    }

    @Test
    public void getCommentByIdTest() throws Exception {
        when(commentService.getCommentById(eq(3))).thenReturn(COMMENT_DTO);
        this.mockMvc.perform(get("/api/comments/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(JsonConverter.asJsonString(COMMENT_DTO)));
    }

    @Test
    public void createCommentTest() throws Exception {
        when(commentService.createComment(COMMENT_DTO)).thenReturn(COMMENT_DTO);
        this.mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConverter.asJsonString(COMMENT_DTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(JsonConverter.asJsonString(COMMENT_DTO)));
    }

    @Test
    public void deleteCommentTest() throws Exception {
        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isOk());
        verify(commentService).deleteComment(1);
    }

    @Test
    public void updateCommentTest() throws Exception {
        when(commentService.updateComment(eq(1), eq(COMMENT_DTO))).thenReturn(COMMENT_DTO);
        mockMvc.perform(put("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConverter.asJsonString(COMMENT_DTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(JsonConverter.asJsonString(COMMENT_DTO)));
        verify(commentService).updateComment(eq(1), eq(COMMENT_DTO));
    }
}
