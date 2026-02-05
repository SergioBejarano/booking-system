package escuelaing.ieti.bookingSystem.service;

import escuelaing.ieti.bookingSystem.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class InMemoryUserService implements UserService {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User create(User user) {
        String userId = user.getId();
        if (userId == null || userId.isBlank() || users.containsKey(userId)) {
            userId = UUID.randomUUID().toString();
        }
        User newUser = new User(userId, user.getName(), user.getEmail());
        users.put(userId, newUser);
        return newUser;
    }

    @Override
    public Optional<User> update(String id, User user) {
        User existingUser = users.get(id);
        if (existingUser == null) {
            return Optional.empty();
        }

        User updatedUser = new User(
                id,
                user.getName() != null ? user.getName() : existingUser.getName(),
                user.getEmail() != null ? user.getEmail() : existingUser.getEmail());

        users.put(id, updatedUser);
        return Optional.of(updatedUser);
    }

    @Override
    public boolean delete(String id) {
        return users.remove(id) != null;
    }
}
