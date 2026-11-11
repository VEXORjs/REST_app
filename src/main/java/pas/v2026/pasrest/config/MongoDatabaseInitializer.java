package pas.v2026.pasrest.config;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ValidationOptions;
import org.bson.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MongoDatabaseInitializer implements CommandLineRunner {

    private final MongoDatabase database;

    public MongoDatabaseInitializer(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public void run(String... args) {

        database.drop();

        createUsersCollection();
        createResourceCollection();
        createAllocationCollection();

        database.getCollection("users").createIndex(
                Indexes.ascending("login"),
                new IndexOptions().unique(true)
        );

        System.out.println("MongoDB: Baza została zresetowana (tryb nietrwały), kolekcje, ograniczenia (not-null) i indeksy utworzone.");
    }

    private void createUsersCollection() {
        String schemaJson = """
                {
                    "$jsonSchema": {
                        "bsonType": "object",
                        "required": ["_id", "login", "password", "type", "active"]
                    }
                }
                """;
                ValidationOptions validationOptions = new ValidationOptions()
                        .validator(Document.parse(schemaJson));
        database.createCollection("users", new CreateCollectionOptions().validationOptions(validationOptions));
    }

    private void createResourceCollection() {
        String schemaJson = """
                {
                    "$jsonSchema": {
                        "bsonType": "object",
                        "required": ["_id", "type", "price", "available"]
                    }
                }
                """;
                ValidationOptions validationOptions = new ValidationOptions()
                        .validator(Document.parse(schemaJson));
        database.createCollection("resources", new CreateCollectionOptions().validationOptions(validationOptions));
    }

    private void createAllocationCollection() {
        String schemaJson = """
                {
                    "$jsonSchema": {
                    "bsonType": "object",
                    "required": ["_id", "clientId", "resourceId", "startTime"]
                    }
                }
                """;
                ValidationOptions validationOptions = new ValidationOptions()
                        .validator(Document.parse(schemaJson));
        database.createCollection("allocations", new CreateCollectionOptions().validationOptions(validationOptions));
    }
}