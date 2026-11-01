package repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Repository<Type> {
    private Map<UUID, Type> gpuMap =  new HashMap<>();

    public void add(UUID id, Type Objecttype) {
        gpuMap.put(id, Objecttype);
    }

    public Type findById(UUID id) {
        return gpuMap.get(id);
    }
}
