package dev.triacontakaihenagon.tasktrackerapi.service;

import dev.triacontakaihenagon.tasktrackerapi.entity.Role;
import dev.triacontakaihenagon.tasktrackerapi.entity.User;
import dev.triacontakaihenagon.tasktrackerapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock UserRepository userRepository;
    @InjectMocks CustomUserDetailsService service;

    @Test
    void loadUserByUsername_returnsUserDetailsWithRole() {
        User user = new User();
        user.setUserName("alice");
        user.setPassword("hashed");
        user.setRole(Role.ADMIN);

        when(userRepository.findByUserName("alice")).thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername("alice");

        assertThat(result.getUsername()).isEqualTo("alice");
        assertThat(result.getAuthorities())
                .extracting(a -> a.getAuthority())
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    void loadUserByUsername_throwsWhenNotFound() {
        when(userRepository.findByUserName("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("ghost"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}