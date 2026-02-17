package escuelaing.ieti.bookingSystem.repository;

import escuelaing.ieti.bookingSystem.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
