package pas.v2026.pasrest.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import pas.v2026.pasrest.model.ResourceAllocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class MongoResourceAllocationRepository implements ResourceAllocationRepository {

    private final MongoCollection<Document> collection;

    public MongoResourceAllocationRepository(MongoDatabase database) {
        this.collection = database.getCollection("allocations");
    }

    @Override
    public ResourceAllocation save(ResourceAllocation allocation) {

        Document document = new Document()
                .append("clientId", allocation.getClientId())
                .append("resourceId", allocation.getResourceId())
                .append("startTime", allocation.getStartTime())
                .append("endTime", allocation.getEndTime());

        if (allocation.getId() == null) {
            collection.insertOne(document);
            allocation.setId(document.getObjectId("_id").toHexString());
        } else {
            collection.replaceOne(
                    Filters.eq("_id", new ObjectId(allocation.getId())),
                    document,
                    new ReplaceOptions().upsert(true)
            );
        }
        return allocation;
    }

    private ResourceAllocation fromDocument(Document d) {
        return ResourceAllocation.builder()
                .id(d.getObjectId("_id").toHexString())
                .clientId(d.getString("clientId"))
                .resourceId(d.getString("resourceId"))
                .startTime(d.getDate("startTime"))
                .endTime(d.getDate("endTime"))
                .build();
    }

    @Override
    public Optional<ResourceAllocation> findById(String id) {
        Document d = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return Optional.ofNullable(d).map(this::fromDocument);
    }

    @Override
    public List<ResourceAllocation> findByClientId(String clientId) {
        return findByFilter("clientId", clientId);
    }

    @Override
    public List<ResourceAllocation> findByResourceId(String resourceId) {
        return findByFilter("resourceId", resourceId);
    }

    private List<ResourceAllocation> findByFilter(String field, String id) {
        List<ResourceAllocation> result = new ArrayList<>();

        for (Document d : collection.find(
                Filters.eq(field, id))) {
            result.add(fromDocument(d));
        }

        return result;
    }

    @Override
    public List<ResourceAllocation> findCurrentByClientId(String id, Date now) {
        return findCurrent("clientId", id, now);
    }

    @Override
    public List<ResourceAllocation> findPastByClientId(String id, Date now) {
        return findPast("clientId", id, now);
    }

    @Override
    public List<ResourceAllocation> findCurrentByResourceId(String id, Date now) {
        return findCurrent("resourceId", id, now);
    }

    @Override
    public List<ResourceAllocation> findPastByResourceId(String id, Date now) {
        return findPast("resourceId", id, now);
    }

    private List<ResourceAllocation> findCurrent(
            String field, String id, Date now) {

        return findWithDateFilter(field, id,
                Filters.and(
                        Filters.lte("startTime", now),
                        Filters.gt("endTime", now)
                ));
    }

    private List<ResourceAllocation> findPast(
            String field, String id, Date now) {

        return findWithDateFilter(field, id,
                Filters.lte("endTime", now));
    }

    private List<ResourceAllocation> findWithDateFilter(
            String field, String id,
            org.bson.conversions.Bson dateFilter) {

        List<ResourceAllocation> result = new ArrayList<>();

        for (Document d : collection.find(
                Filters.and(Filters.eq(field, id), dateFilter))) {
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
    public void deleteById(String id) {
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

    @Override
    public boolean hasOverlappingAllocations(String resourceId, Date startTime, Date endTime) {
        long count = collection.countDocuments(
                Filters.and(
                        Filters.eq("resourceId", resourceId),
                        Filters.lt("startTime", endTime),
                        Filters.gt("endTime", startTime)
                )
        );
        return count > 0;
    }
}