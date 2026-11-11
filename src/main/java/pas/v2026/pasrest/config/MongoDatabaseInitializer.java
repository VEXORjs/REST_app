package pas.v2026.pasrest.config;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ValidationOptions;
import org.bson.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pas.v2026.pasrest.model.*;
import pas.v2026.pasrest.repositories.ResourceAllocationRepository;
import pas.v2026.pasrest.repositories.ResourceRepository;
import pas.v2026.pasrest.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class MongoDatabaseInitializer implements CommandLineRunner {

    private final MongoDatabase database;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceAllocationRepository allocationRepository;

    public MongoDatabaseInitializer(MongoDatabase database,
                                    UserRepository userRepository,
                                    ResourceRepository resourceRepository,
                                    ResourceAllocationRepository allocationRepository) {
        this.database = database;
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
        this.allocationRepository = allocationRepository;
    }

    @Override
    public void run(String... args) {
        
        dropAllCollections();

        createUsersCollection();
        createResourceCollection();
        createAllocationCollection();

        database.getCollection("users").createIndex(
                Indexes.ascending("login"),
                new IndexOptions().unique(true)
        );

        System.out.println("MongoDB: Baza została zresetowana (tryb nietrwały), kolekcje, ograniczenia (not-null) i indeksy utworzone.");

        seedInitialData();
    }

    private void dropAllCollections() {
        for (String collectionName: database.listCollectionNames()) {
            database.getCollection(collectionName).drop();
        }
    }

    private void seedInitialData() {
        System.out.println("=== INICJALIZACJA DANYCH TESTOWYCH ===");

        UserAdmin admin = UserAdmin.builder()
                .login("admin")
                .password("admin123")
                .active(true)
                .build();

        ResourceManager manager = ResourceManager.builder()
                .login("manager")
                .password("man123")
                .active(true)
                .build();

        Client client1 = Client.builder()
                .login("klient_aktywny")
                .password("pass123")
                .active(true)
                .build();

        Client client2 = Client.builder()
                .login("klient_zablokowany")
                .password("pass123")
                .active(false)
                .build();

        Server server = Server.builder()
                .name("Web-Server-01")
                .description("Główny serwer NGINX")
                .price(BigDecimal.valueOf(150.0))
                .manufacturer("Dell")
                .cpuName("Intel Xeon")
                .ram(64)
                .storageCapacity(2000)
                .build();

        DataCenter dataCenter = DataCenter.builder()
                .name("DC-Warszawa")
                .description("Centrum danych w stolicy")
                .price(BigDecimal.valueOf(5000.0))
                .location("Warszawa")
                .area(500.0)
                .powerCapacity(1000)
                .build();

        Cluster cluster = Cluster.builder()
                .name("Cluster-AI-Alpha")
                .description("Klaster obliczeniowy do zadań uczenia maszynowego")
                .price(BigDecimal.valueOf(12000.0))
                .numberOfServers(16)
                .architecture("x86_64")
                .build();

        userRepository.save(admin);
        userRepository.save(client1);
        userRepository.save(client2);
        userRepository.save(manager);

        resourceRepository.save(server);
        resourceRepository.save(dataCenter);
        resourceRepository.save(cluster);

        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() + (7L * 24 * 60 * 60 * 1000));

        ResourceAllocation allocation = ResourceAllocation.builder()
                .clientId(client1.getId())
                .resourceId(server.getId())
                .startTime(startTime)
                .endTime(endTime)
                .build();

        allocationRepository.save(allocation);

        System.out.println("=== INICJALIZACJA DANYCH TESTOWYCH ZAKOŃCZONA ===");
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