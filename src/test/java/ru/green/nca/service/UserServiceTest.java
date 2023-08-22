package ru.green.nca.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.green.nca.entity.User;
import ru.green.nca.repository.UserRepository;
import ru.green.nca.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    User USER = new User(1, "TheGreenUp", "12345678","Даниил",
            "Гринь","Сергеевич",null,null);

    @Test
    public void getByIdTest()  {
        when(userRepository.findById(eq(5))).thenReturn(Optional.of(USER));
        User userById = this.userService.getUserById(5);
        assertNotNull(userById); // Проверка, что полученный объект не null
        assertEquals(USER.getId(), userById.getId()); // Проверка, что id совпадает
        assertEquals(USER.getName(), userById.getName()); // Проверка, что title совпадает
    }

    @Test
    public void getAllUserTest()  {
        Pageable pageable = PageRequest.of(0, 10); // Создаем объект Pageable для пагинации
        Page<User> UserPage = new PageImpl<>(List.of(USER)); // Создаем объект Page с данными
        when(userRepository.findAll(pageable)).thenReturn(UserPage); // Моделируем поведение репозитория

        List<User> User = this.userService.getAllUsers(0, 10);

        assertNotNull(User); // Проверка, что полученный объект не null
        assertEquals(1, User.size()); // Проверка, что список содержит 1 элемент
        assertEquals(USER, User.get(0)); // Проверка, что элемент соответствует ожидаемому
    }

    @Test
    public void createUserTest()  {
        when(userRepository.save(eq(USER))).thenReturn(USER);
        User User = this.userService.createUser(USER);
        assertNotNull(User); // Проверка, что полученный объект не null
        assertEquals(USER.getId(), User.getId()); // Проверка, что id совпадает
        assertEquals(USER.getName(), User.getName()); // Проверка, что title совпадает
    }

    @Test
    public void updateUserTest()  {
        // Подготовьте мок repository
        when(userRepository.findById(1)).thenReturn(Optional.of(USER)); // Симулируем наличие пользователя с ID 1
        when(userRepository.save(USER)).thenReturn(USER);

        // Вызываем метод обновления
        User updatedUser = userService.updateUser(1, USER);

        // Проверки
        assertNotNull(updatedUser);
        assertEquals(USER.getId(), updatedUser.getId());
        assertEquals(USER.getName(), updatedUser.getName());
        // Добавьте другие проверки, если необходимо
    }

    @Test
    public void deleteUserTest()  {
        // Mock the behavior of the repository's deleteById method
        doNothing().when(userRepository).deleteById(5);
        // Call the service method to delete the User
        userService.deleteUser(5);
        // Verify that the delete method was called with the correct ID
        verify(userRepository, times(1)).deleteById(5);
    }
}
