package pas.v2026.pasrest.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import pas.v2026.pasrest.model.ResourceAllocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MongoResourceAllocationRepository
        implements ResourceAllocationRepository {

    private final MongoCollection<Document> collection;

    public MongoResourceAllocationRepository(MongoDatabase database) {
        this.collection = database.getCollection("allocations");
    }

    @Override
    public ResourceAllocation save(ResourceAllocation allocation) {
        if (allocation.getId() == null) {
            allocation.setId(UUID.randomUUID());
        }

        Document document = new Document("_id", allocation.getId().toString())
                .append("clientId", allocation.getClientId().toString())
                .append("resourceId", allocation.getResourceId().toString())
                .append("startTime", allocation.getStartTime())
                .append("endTime", allocation.getEndTime());

        collection.replaceOne(
                Filters.eq("_id", allocation.getId().toString()),
                document,
                new ReplaceOptions().upsert(true)
        );

        return allocation;
    }

    private ResourceAllocation fromDocument(Document d) {
        return ResourceAllocation.builder()
                .id(UUID.fromString(d.getString("_id")))
                .clientId(UUID.fromString(d.getString("clientId")))
                .resourceId(UUID.fromString(d.getString("resourceId")))
                .startTime(d.getDate("startTime"))
                .endTime(d.getDate("endTime"))
                .build();
    }

    @Override
    public Optional<ResourceAllocation> findById(UUID id) {
        Document d = collection.find(Filters.eq("_id", id.toString())).first();
        return Optional.ofNullable(d).map(this::fromDocument);
    }

    @Override
    public List<ResourceAllocation> findByClientId(UUID clientId) {
        return findByFilter("clientId", clientId);
    }

    @Override
    public List<ResourceAllocation> findByResourceId(UUID resourceId) {
        return findByFilter("resourceId", resourceId);
    }

    private List<ResourceAllocation> findByFilter(String field, UUID id) {
        List<ResourceAllocation> result = new ArrayList<>();

        for (Document d : collection.find(
                Filters.eq(field, id.toString()))) {
            result.add(fromDocument(d));
        }

        return result;
    }

    @Override
    public List<ResourceAllocation> findCurrentByClientId(UUID id, Date now) {
        return findCurrent("clientId", id, now);
    }

    @Override
    public List<ResourceAllocation> findPastByClientId(UUID id, Date now) {
        return findPast("clientId", id, now);
    }

    @Override
    public List<ResourceAllocation> findCurrentByResourceId(UUID id, Date now) {
        return findCurrent("resourceId", id, now);
    }

    @Override
    public List<ResourceAllocation> findPastByResourceId(UUID id, Date now) {
        return findPast("resourceId", id, now);
    }

    private List<ResourceAllocation> findCurrent(
            String field, UUID id, Date now) {

        return findWithDateFilter(field, id,
                Filters.gt("endTime", now));
    }

    private List<ResourceAllocation> findPast(
            String field, UUID id, Date now) {

        return findWithDateFilter(field, id,
                Filters.lte("endTime", now));
    }

    private List<ResourceAllocation> findWithDateFilter(
            String field, UUID id,
            org.bson.conversions.Bson dateFilter) {

        List<ResourceAllocation> result = new ArrayList<>();

        for (Document d : collection.find(
                Filters.and(Filters.eq(field, id.toString()), dateFilter))) {
            result.add(fromDocument(d));
        }

        return result;
    }

    @Override
    public List<ResourceAllocation> findAll() {
        List<ResourceAllocation> result = new ArrayList<>();

        for (Document d : collection.find()) {
            result.add(fromDocument(d));
        }

        return result;
    }

    @Override
    public void deleteById(UUID id) {
        collection.deleteOne(Filters.eq("_id", id.toString()));
    }
}