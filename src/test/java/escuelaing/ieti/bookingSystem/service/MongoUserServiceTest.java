package escuelaing.ieti.bookingSystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import escuelaing.ieti.bookingSystem.model.User;
import escuelaing.ieti.bookingSystem.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MongoUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MongoUserService userService;

    @BeforeEach
    void setUp() {
        userService = new MongoUserService(userRepository, passwordEncoder);
    }

    @Test
    void getAllShouldReturnRepositoryResult() {
        List<User> expectedUsers = List.of(new User("1", "Ana", "ana@example.com", null));
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> users = userService.getAll();

        assertThat(users).isEqualTo(expectedUsers);
        verify(userRepository).findAll();
    }

    @Test
    void getByIdShouldReturnUserWhenExists() {
        User storedUser = new User("1", "Ana", "ana@example.com", null);
        when(userRepository.findById("1")).thenReturn(Optional.of(storedUser));

        Optional<User> result = userService.getById("1");

        assertThat(result).contains(storedUser);
        verify(userRepository).findById("1");
    }

    @Test
    void createShouldEncodePasswordWhenProvided() {
        User requestUser = new User(null, "Ana", "ana@example.com", "plainPass");
        when(passwordEncoder.encode("plainPass")).thenReturn("encodedPass");
        when(userRepository.save(requestUser)).thenReturn(requestUser);

        User created = userService.create(requestUser);

        assertThat(created.getPassword()).isEqualTo("encodedPass");
        verify(passwordEncoder).encode("plainPass");
        verify(userRepository).save(requestUser);
        verifyNoMoreInteractions(passwordEncoder, userRepository);
    }

    @Test
    void updateShouldMergeProvidedFields() {
        User existing = new User("1", "Ana", "ana@example.com", "encodedOld");
        User update = new User(null, "Ana Maria", "ana.maria@example.com", "newPass");
        when(userRepository.findById("1")).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNew");
        when(userRepository.save(existing)).thenReturn(existing);

        Optional<User> result = userService.update("1", update);

        assertThat(result).isPresent();
        assertThat(existing.getName()).isEqualTo("Ana Maria");
        assertThat(existing.getEmail()).isEqualTo("ana.maria@example.com");
        assertThat(existing.getPassword()).isEqualTo("encodedNew");
        verify(userRepository).findById("1");
        verify(passwordEncoder).encode("newPass");
        verify(userRepository).save(existing);
    }

    @Test
    void deleteShouldReturnTrueWhenUserExists() {
        when(userRepository.existsById("1")).thenReturn(true);

        boolean deleted = userService.delete("1");

        assertThat(deleted).isTrue();
        verify(userRepository).existsById("1");
        verify(userRepository).deleteById("1");
    }

    @Test
    void deleteShouldReturnFalseWhenUserDoesNotExist() {
        when(userRepository.existsById("2")).thenReturn(false);

        boolean deleted = userService.delete("2");

        assertThat(deleted).isFalse();
        verify(userRepository).existsById("2");
        verifyNoMoreInteractions(userRepository);
    }
}
