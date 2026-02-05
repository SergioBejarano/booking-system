package escuelaing.ieti.bookingSystem.service;

import escuelaing.ieti.bookingSystem.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();

    Optional<User> getById(String id);

    User create(User user);

    Optional<User> update(String id, User user);

    boolean delete(String id);
}
