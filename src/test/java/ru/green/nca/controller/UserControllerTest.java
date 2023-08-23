package ru.green.nca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.green.nca.dto.UserDto;
import ru.green.nca.entity.User;
import ru.green.nca.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    User USER = new User(1, "TheGreenUp", "12345678","Даниил",
            "Гринь","Сергеевич",null,null,1);
    UserDto USER_DTO = new UserDto(1, "TheGreenUp", "12345678","Даниил",
            "Гринь","Сергеевич",null,null,1);
    @Test
    public void getAllUserTest() throws Exception {
        when(userService.getAllUsers(eq(0), eq(10))).thenReturn(List.of(USER));
        //сверху мы описываем поведение, что будет, если передадим 0 и 10 в качестве параметров
        //а снизу мы вызываем с этими параметрами, на что получаем результат из thenReturn()
        this.mockMvc.perform(get("/api/users?page=0&size=10"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUserByIdTest() throws Exception {
        when(userService.getUserById(eq(3))).thenReturn(USER);
        this.mockMvc.perform(get("/api/users/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createUserTest() throws Exception {
        when(userService.createUser(USER)).thenReturn(USER);
        String UserJson = asJsonString(USER);
        this.mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserJson))
                .andDo(print())
                .andExpect(status().isOk());
        //TODO и тут тоже
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
        verify(userService).deleteUser(1);
    }

    @Test
    public void updateUserTest() throws Exception {
        when(userService.updateUser(eq(1), eq(USER))).thenReturn(USER);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(USER_DTO)))
                .andExpect(status().isOk());

        verify(userService).updateUser(eq(1), eq(USER));
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
