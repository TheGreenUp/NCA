package ru.green.nca.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.green.nca.dto.UserDto;
import ru.green.nca.entity.User;
import ru.green.nca.enums.UserRole;
import ru.green.nca.repository.UserRepository;
import ru.green.nca.security.UserDetailsImpl;
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
    @Mock
    private ModelMapper modelMapper;

    User USER = new User(1, "TheGreenUp", "encodedPassword", "Даниил",
            "Гринь", "Сергеевич", null, null, UserRole.ADMIN);
    UserDto USER_DTO = new UserDto(1, "TheGreenUp", "encodedPassword", "Даниил",
            "Гринь", "Сергеевич", null, null, UserRole.ADMIN);

    @Test
    public void getByIdTest() {
        when(userRepository.findById(any())).thenReturn(Optional.of(USER));
        when(modelMapper.map(any(), any())).thenReturn(USER_DTO);

        UserDto userById = userService.getUserById(5);
        assertNotNull(userById);
        assertEquals(USER.getId(), userById.getId());
        assertEquals(USER.getName(), userById.getName());
    }

    @Test
    public void getAllUserTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> UserPage = new PageImpl<>(List.of(USER));
        when(userRepository.findAll(pageable)).thenReturn(UserPage);

        List<User> User = this.userService.getAllUsers(0, 10);

        assertNotNull(User);
        assertEquals(1, User.size());
        assertEquals(USER, User.get(0));
    }

    @Test
    public void createUserTest() {
        securityConfiguration();
        PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
        when(passwordEncoderMock.encode(any(CharSequence.class))).thenReturn("encodedPassword");

        UserService userService = new UserServiceImpl(userRepository, passwordEncoderMock, modelMapper);

        when(userRepository.save(eq(USER))).thenReturn(USER);

        User createdUser = userService.createUser(USER_DTO);

        assertNotNull(createdUser);
        assertEquals(USER.getId(), createdUser.getId());
        assertEquals(USER.getName(), createdUser.getName());
    }


    @Test
    public void updateUserTest() {
        securityConfiguration();

        PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
        when(passwordEncoderMock.encode(any(CharSequence.class))).thenReturn("encodedPassword");

        UserServiceImpl userService = new UserServiceImpl(userRepository, passwordEncoderMock, modelMapper);


        when(userRepository.findById(1)).thenReturn(Optional.of(USER));
        when(userRepository.save(USER)).thenReturn(USER);

        User updatedUser = userService.updateUser(1, USER_DTO);

        assertNotNull(updatedUser);
        assertEquals(USER.getId(), updatedUser.getId());
        assertEquals(USER.getName(), updatedUser.getName());
    }

    @Test
    public void deleteUserTest() {
        doNothing().when(userRepository).deleteById(5);
        userService.deleteUser(5);
        verify(userRepository, times(1)).deleteById(5);
    }

    private void securityConfiguration() {
        User testUser = USER;
        UserDetails userDetails = new UserDetailsImpl(testUser); // Создаем UserDetailsImpl для тестового пользователя
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication); // Устанавливаем аутентификацию для текущего теста

    }
}
