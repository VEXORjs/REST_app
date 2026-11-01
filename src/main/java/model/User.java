package model;

import java.util.Objects;
import java.util.UUID;

public abstract class User {
    private String login;
    private String password;
    private boolean active;
    private final UUID id;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.id = UUID.randomUUID();
        this.active = false;
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
