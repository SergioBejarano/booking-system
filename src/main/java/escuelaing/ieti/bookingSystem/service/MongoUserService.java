package escuelaing.ieti.bookingSystem.service;

import escuelaing.ieti.bookingSystem.model.User;
import escuelaing.ieti.bookingSystem.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MongoUserService implements UserService {

    private final UserRepository userRepository;

    public MongoUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User create(User user) {
        user.setId(null);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> update(String id, User user) {
        return userRepository.findById(id).map(existing -> {
            if (user.getName() != null && !user.getName().isBlank()) {
                existing.setName(user.getName());
            }
            if (user.getEmail() != null && !user.getEmail().isBlank()) {
                existing.setEmail(user.getEmail());
            }
            return userRepository.save(existing);
        });
    }

    @Override
    public boolean delete(String id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }
}
