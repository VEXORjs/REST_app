package pas.v2026.pasrest.repositories;

import pas.v2026.pasrest.exceptions.LoginAlreadyInUseException;
import pas.v2026.pasrest.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<UUID, User> users = new ConcurrentHashMap<>();

    @Override
    public synchronized User save(User user) {
        findByLoginExact(user.getLogin())
                .filter(existing -> !existing.getId().equals(user.getId()))
                .ifPresent(ignored -> {
                    throw new LoginAlreadyInUseException(user.getLogin());
                });

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByLoginExact(String login) {
        return users.values().stream()
                .filter(u -> u.getLogin().equalsIgnoreCase(login))
                .findFirst();
    }

    @Override
    public List<User> findByLoginPartial(String loginPart) {
        return users.values().stream()
                .filter(u -> u.getLogin().toLowerCase().contains(loginPart.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public ArrayList<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
