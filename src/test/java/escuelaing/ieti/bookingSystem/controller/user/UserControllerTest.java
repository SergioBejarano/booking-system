package escuelaing.ieti.bookingSystem.controller.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import escuelaing.ieti.bookingSystem.model.User;
import escuelaing.ieti.bookingSystem.service.UserService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getAllUsersShouldReturnOkResponse() {
        List<User> users = List.of(new User("1", "Jane", "jane@example.com", null));
        when(userService.getAll()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(users);
        verify(userService).getAll();
    }

    @Test
    void getUserByIdShouldReturnNotFoundWhenMissing() {
        when(userService.getById("10")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById("10");

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(userService).getById("10");
    }

    @Test
    void getUserByIdShouldReturnUserWhenExists() {
        User stored = new User("1", "Jane", "jane@example.com", null);
        when(userService.getById("1")).thenReturn(Optional.of(stored));

        ResponseEntity<User> response = userController.getUserById("1");

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(stored);
    }

    @Test
    void createUserShouldReturnCreatedWithLocation() {
        User request = new User(null, "Jane", "jane@example.com", null);
        User created = new User("123", "Jane", "jane@example.com", null);
        when(userService.create(any(User.class))).thenReturn(created);

        ResponseEntity<User> response = userController.createUser(request);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getHeaders().getLocation()).hasToString("/api/v1/users/123");
        assertThat(response.getBody()).isEqualTo(created);
        verify(userService).create(request);
    }

    @Test
    void updateUserShouldReturnNotFoundWhenMissing() {
        when(userService.update("1", null)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.updateUser("1", null);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        verify(userService).update("1", null);
    }

    @Test
    void updateUserShouldReturnUpdatedEntity() {
        User updated = new User("1", "Jane", "jane@example.com", null);
        when(userService.update("1", updated)).thenReturn(Optional.of(updated));

        ResponseEntity<User> response = userController.updateUser("1", updated);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(updated);
    }

    @Test
    void deleteUserShouldReturnNoContentWhenRemoved() {
        when(userService.delete("1")).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUser("1");

        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void deleteUserShouldReturnNotFoundWhenMissing() {
        when(userService.delete("2")).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUser("2");

        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
