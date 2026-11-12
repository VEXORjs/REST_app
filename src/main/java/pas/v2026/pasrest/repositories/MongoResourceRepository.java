package pas.v2026.pasrest.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import pas.v2026.pasrest.model.*;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.regex.Pattern;

@Repository
public class MongoResourceRepository implements ResourceRepository {
    private final MongoCollection<Document> collection;
    private final ObjectMapper objectMapper;

    public MongoResourceRepository(MongoDatabase database, ObjectMapper objectMapper) {
        this.collection = database.getCollection("resources");
        this.objectMapper = objectMapper;
    }

    @Override
    public Resource save(Resource resource) {
        Map<String, Object> resourceMap = objectMapper.convertValue(resource, new TypeReference<Map<String, Object>>() {});
        Document document = new Document(resourceMap);

        if(resource.getId() == null) {
            collection.insertOne(document);
            resource.setId(document.getObjectId("_id").toHexString());
        } else {
            collection.replaceOne(
                    Filters.eq("_id", new ObjectId(resource.getId())),
                    document,
                    new ReplaceOptions().upsert(true)
            );
        }
        return resource;
    }

    private Resource fromDocument(Document d) {
        d.put("id", d.getObjectId("_id").toHexString());
        return objectMapper.convertValue(d, Resource.class);
    }

    @Override
    public Optional<Resource> findById(String id) {
        Document d = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return Optional.ofNullable(d).map(this::fromDocument);
    }

    @Override
    public List<Resource> findAll() {
        List<Resource> result = new ArrayList<>();

        for (Document d : collection.find()) {
            result.add(fromDocument(d));
        }

        return result;
    }

    @Override
    public List<Resource> findByNamePartial(String namePart) {
        List<Resource> result = new ArrayList<>();
        for (Document d : collection.find(Filters.regex("name", Pattern.quote(namePart), "i"))) {
            result.add(fromDocument(d));
        }
        return result;
    }

    @Override
    public List<Resource> findByAvailable(boolean available) {
        List<Resource> result = new ArrayList<>();
        for (Document d : collection.find(Filters.eq("available", "i"))) {
            result.add(fromDocument(d));
        }
        return result;
    }

    @Override
    public void deleteById(String id) {
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }
}