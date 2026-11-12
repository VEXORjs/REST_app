package pas.v2026.pasrest.repositories;

import pas.v2026.pasrest.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(String id);
    Optional<User> findByLoginExact(String login);
    List<User> findByLoginPartial(String loginPart);
    List<User> findAll();
}
