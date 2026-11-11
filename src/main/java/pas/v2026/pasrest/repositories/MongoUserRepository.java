
package pas.v2026.pasrest.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import pas.v2026.pasrest.exceptions.LoginAlreadyInUseException;
import pas.v2026.pasrest.exceptions.UnknownUserTypeException;
import pas.v2026.pasrest.model.Client;
import pas.v2026.pasrest.model.ResourceManager;
import pas.v2026.pasrest.model.User;
import pas.v2026.pasrest.model.UserAdmin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MongoUserRepository implements UserRepository {

    private final MongoCollection<Document> users;

    public MongoUserRepository(MongoDatabase database) {
        this.users = database.getCollection("users");
    }

    private String getUserType(User user) {
        if (user instanceof Client) {
            return "CLIENT";
        }
        if (user instanceof UserAdmin) {
            return "ADMIN";
        }
        if (user instanceof ResourceManager) {
            return "MANAGER";
        }

        throw new IllegalArgumentException("Nieznany typ użytkownika");
    }

    @Override
    public synchronized User save(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }

        Optional<User> existing = findByLoginExact(user.getLogin());

        if (existing.isPresent() &&
                !existing.get().getId().equals(user.getId())) {
            throw new LoginAlreadyInUseException(user.getLogin());
        }

        Document document = new Document("_id", user.getId().toString())
                .append("login", user.getLogin())
                .append("password", user.getPassword())
                .append("active", user.isActive())
                .append("type", getUserType(user));

        users.replaceOne(
                Filters.eq("_id", user.getId().toString()),
                document,
                new ReplaceOptions().upsert(true)
        );

        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        Document document = users.find(
                Filters.eq("_id", id.toString())
        ).first();

        return Optional.ofNullable(document).map(this::toUser);
    }

    @Override
    public Optional<User> findByLoginExact(String login) {
        Document document = users.find(
                Filters.eq("login", login)
        ).collation(
                com.mongodb.client.model.Collation.builder()
                        .locale("en")
                        .collationStrength(
                                com.mongodb.client.model.CollationStrength.SECONDARY
                        )
                        .build()
        ).first();

        return Optional.ofNullable(document).map(this::toUser);
    }

    @Override
    public List<User> findByLoginPartial(String loginPart) {
        return users.find(
                Filters.regex("login", java.util.regex.Pattern.quote(loginPart), "i")
        ).map(this::toUser).into(new java.util.ArrayList<>());
    }

    @Override
    public List<User> findAll() {
        return users.find()
                .map(this::toUser)
                .into(new java.util.ArrayList<>());
    }

    private User toUser(Document document) {
        String type = document.getString("type");

        UUID id = UUID.fromString(document.getString("_id"));
        String login = document.getString("login");
        String password = document.getString("password");
        boolean active = document.getBoolean("active", false);

        return switch (type) {
            case "CLIENT" -> Client.builder()
                    .id(id)
                    .login(login)
                    .password(password)
                    .active(active)
                    .build();

            case "ADMIN" -> UserAdmin.builder()
                    .id(id)
                    .login(login)
                    .password(password)
                    .active(active)
                    .build();

            case "MANAGER" -> ResourceManager.builder()
                    .id(id)
                    .login(login)
                    .password(password)
                    .active(active)
                    .build();

            default -> throw new UnknownUserTypeException(
                    "Nieznany typ użytkownika: " + type
            );
        };
    }
}
