
package pas.v2026.pasrest.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import pas.v2026.pasrest.exceptions.LoginAlreadyInUseException;
import pas.v2026.pasrest.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MongoUserRepository implements UserRepository {

    private final MongoCollection<Document> users;
    private final ObjectMapper objectMapper;

    public MongoUserRepository(MongoDatabase database, ObjectMapper objectMapper) {
        this.users = database.getCollection("users");
        this.objectMapper = objectMapper;
    }

    @Override
    public synchronized User save(User user) {
        Optional<User> existing = findByLoginExact(user.getLogin());

        if (existing.isPresent() && !existing.get().getId().equals(user.getId())) {
            throw new LoginAlreadyInUseException(user.getLogin());
        }

        Map<String, Object> userMap = objectMapper.convertValue(user, new TypeReference<Map<String, Object>>() {});
        Document document = new Document(userMap);

        if (user.getId() == null) {
            users.insertOne(document);
            user.setId(document.getObjectId("_id").toHexString());
        } else {
            users.replaceOne(
                    Filters.eq("_id", new ObjectId(user.getId())),
                    document,
                    new ReplaceOptions().upsert(true)
            );
        }
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        Document document = users.find(
                Filters.eq("_id", new ObjectId(id))
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
        document.put("id", document.getObjectId("_id").toHexString());
        return objectMapper.convertValue(document, User.class);
    }
}
