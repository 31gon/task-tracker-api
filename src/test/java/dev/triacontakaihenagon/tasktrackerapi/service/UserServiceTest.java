package dev.triacontakaihenagon.tasktrackerapi.service;

import dev.triacontakaihenagon.tasktrackerapi.dto.UserRequest;
import dev.triacontakaihenagon.tasktrackerapi.entity.Role;
import dev.triacontakaihenagon.tasktrackerapi.entity.User;
import dev.triacontakaihenagon.tasktrackerapi.exception.UserNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUserName("alice");
        user.setRole(Role.USER);
    }

    @Test
    void createUser_encodesPasswordAndDefaultsRole() {
        UserRequest request = new UserRequest();
        request.setUserName("alice");
        request.setPassword("plain");

        when(passwordEncoder.encode("plain")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.createUser(request);

        assertThat(result.getUserName()).isEqualTo("alice");
        assertThat(result.getPassword()).isEqualTo("hashed");
        assertThat(result.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void createUser_usesGivenRoleWhenProvided() {
        UserRequest request = new UserRequest();
        request.setUserName("bob");
        request.setPassword("plain");
        request.setRole(Role.ADMIN);

        when(passwordEncoder.encode("plain")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.createUser(request);

        assertThat(result.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void updateUser_updatesFieldsWhenFound() {
        UserRequest request = new UserRequest();
        request.setUserName("alice2");
        request.setRole(Role.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(1L, request);

        assertThat(result.getUserName()).isEqualTo("alice2");
        assertThat(result.getRole()).isEqualTo(Role.ADMIN);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void updateUser_throwsWhenNotFound() {
        when(userRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(404L, new UserRequest()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void deleteUser_deletesWhenExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_throwsWhenNotFound() {
        when(userRepository.existsById(404L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(404L))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void getCurrentUser_returnsUserWhenFound() {
        when(userRepository.findByUserName("alice")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getCurrentUser("alice");

        assertThat(result).contains(user);
    }
}