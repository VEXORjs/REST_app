package pas.v2026.pasrest.services;

import org.springframework.stereotype.Service;
import pas.v2026.pasrest.exceptions.UserNotFoundException;
import pas.v2026.pasrest.model.User;
import pas.v2026.pasrest.repositories.UserRepository;

import java.util.List;
import java.util.UUID;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public synchronized User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByLoginPartial(String loginPart) {
        return userRepository.findByLoginPartial(loginPart);
    }

    public User getUserByLoginExact(String login) {
        return userRepository.findByLoginExact(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    public synchronized User changeUserStatus(UUID id, boolean isActive) {
        User user = getUser(id);
        user.setActive(isActive);
        return userRepository.save(user);
    }

    public synchronized User updateUser(UUID id, String newPassword) {
        User user = getUser(id);
        user.setPassword(newPassword);
        return user;
    }



}
