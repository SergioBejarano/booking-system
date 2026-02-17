package escuelaing.ieti.bookingSystem.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import escuelaing.ieti.bookingSystem.model.User;
import escuelaing.ieti.bookingSystem.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsernameShouldReturnSpringUser() {
        User storedUser = new User("1", "Jane", "jane@example.com", "hash");
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(storedUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("jane@example.com");

        assertThat(userDetails.getUsername()).isEqualTo("jane@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("hash");
    }

    @Test
    void loadUserByUsernameShouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("missing@example.com"));
    }
}
