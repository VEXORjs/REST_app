package pas.v2026.pasrest.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.springframework.stereotype.Repository;
import pas.v2026.pasrest.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MongoResourceRepository implements ResourceRepository {

    private final MongoCollection<Document> collection;

    public MongoResourceRepository(MongoDatabase database) {
        this.collection = database.getCollection("resources");
    }

    @Override
    public Resource save(Resource resource) {
        if (resource.getId() == null) {
            resource.setId(UUID.randomUUID());
        }

        Document document = new Document()
                .append("_id", resource.getId().toString())
                .append("type", getResourceType(resource))
                .append("name", resource.getName())
                .append("description", resource.getDescription())
                .append("price", resource.getPrice())
                .append("available", resource.isAvailable())
                .append("ownerId", resource.getOwnerId() == null
                        ? null : resource.getOwnerId().toString());

        switch (resource) {
            case Server server -> document.append("manufacturer", server.getManufacturer())
                    .append("cpuName", server.getCpuName())
                    .append("numberOfCpus", server.getNumberOfCpus())
                    .append("ram", server.getRam())
                    .append("storageCapacity", server.getStorageCapacity());
            case DataCenter dc -> document.append("location", dc.getLocation())
                    .append("area", dc.getArea())
                    .append("powerCapacity", dc.getPowerCapacity());
            case Cluster cluster -> document.append("numberOfServers", cluster.getNumberOfServers())
                    .append("architecture", cluster.getArchitecture());
            default -> {
            }
        }

        collection.replaceOne(
                Filters.eq("_id", resource.getId().toString()),
                document,
                new ReplaceOptions().upsert(true)
        );

        return resource;
    }

    private String getResourceType(Resource resource) {
        if (resource instanceof Server) return "SERVER";
        if (resource instanceof DataCenter) return "DATA_CENTER";
        if (resource instanceof Cluster) return "CLUSTER";

        throw new IllegalArgumentException("Nieznany typ zasobu");
    }

    private Resource fromDocument(Document d) {
        String type = d.getString("type");

        Resource.ResourceBuilder<?, ?> builder;

        switch (type) {
            case "SERVER" -> {
                return Server.builder()
                        .id(UUID.fromString(d.getString("_id")))
                        .name(d.getString("name"))
                        .description(d.getString("description"))
                        .price(d.get("price", Decimal128.class) != null
                                ? d.get("price", Decimal128.class).bigDecimalValue()
                                : null)
                        .available(d.getBoolean("available", true))
                        .ownerId(parseUUID(d.getString("ownerId")))
                        .manufacturer(d.getString("manufacturer"))
                        .cpuName(d.getString("cpuName"))
                        .numberOfCpus(d.getInteger("numberOfCpus", 1))
                        .ram(d.getInteger("ram"))
                        .storageCapacity(d.getInteger("storageCapacity"))
                        .build();
            }
            case "DATA_CENTER" -> {
                return DataCenter.builder()
                        .id(UUID.fromString(d.getString("_id")))
                        .name(d.getString("name"))
                        .description(d.getString("description"))
                        .price(d.get("price", Decimal128.class) != null
                                ? d.get("price", Decimal128.class).bigDecimalValue()
                                : null)
                        .available(d.getBoolean("available", true))
                        .ownerId(parseUUID(d.getString("ownerId")))
                        .location(d.getString("location"))
                        .area(d.getDouble("area"))
                        .powerCapacity(d.getInteger("powerCapacity"))
                        .build();
            }
            case "CLUSTER" -> {
                return Cluster.builder()
                        .id(UUID.fromString(d.getString("_id")))
                        .name(d.getString("name"))
                        .description(d.getString("description"))
                        .price(d.get("price", Decimal128.class) != null
                                ? d.get("price", Decimal128.class).bigDecimalValue()
                                : null)
                        .available(d.getBoolean("available", true))
                        .ownerId(parseUUID(d.getString("ownerId")))
                        .numberOfServers(d.getInteger("numberOfServers"))
                        .architecture(d.getString("architecture"))
                        .build();
            }
            default -> throw new IllegalArgumentException("Nieznany typ zasobu: " + type);
        }
    }

    private UUID parseUUID(String value) {
        return value == null ? null : UUID.fromString(value);
    }

    @Override
    public Optional<Resource> findById(UUID id) {
        Document d = collection.find(Filters.eq("_id", id.toString())).first();
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
        return findAll().stream()
                .filter(r -> r.getName() != null &&
                        r.getName().toLowerCase()
                                .contains(namePart.toLowerCase()))
                .toList();
    }

    @Override
    public List<Resource> findByAvailable(boolean available) {
        return findAll().stream()
                .filter(r -> r.isAvailable() == available)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        collection.deleteOne(Filters.eq("_id", id.toString()));
    }
}