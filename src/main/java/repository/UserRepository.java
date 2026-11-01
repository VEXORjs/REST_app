package repository;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepository {
    private List<User> users = new ArrayList<>();

    public User findById(UUID id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public User findByLogin(String login) {
        if (login == null) {
            return null;
        }

        for (User user : users) {
            if (user.getLogin() != null && user.getLogin().equalsIgnoreCase(login)) {
                return user;
            }
        }
        return null;
    }

    public List<User> findByContainingLogin(String login) {
        List<User> result = new ArrayList<>();

        for (User user : users) {
            if (user.getLogin().contains(login)) {
                result.add(user);
            }
        }
        return result;
    }

    public List<User> readAllUsers() {
        return users;
    }

    public boolean updateUser(User updatedUser) {
        for (User user : users) {
            if (user.getId().equals(updatedUser.getId())) {
                user.setPassword(updatedUser.getPassword());
                user.setActive(updatedUser.isActive());
                user.setLogin(updatedUser.getLogin());
                return true;
            }
        }
        return false;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void activateUser(UUID id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                user.setActive(true);
            }
        }
    }

    public void deactivateUser(UUID id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                user.setActive(false);
            }
        }
    }
}
