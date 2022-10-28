package org.foxminded.springcourse.consoleapp.dao;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EntityMetaDataCache {

    private final Map<Class<?>, EntityMetaData> cache = new ConcurrentHashMap<>();

    public void put(Class<?> entityClass, EntityMetaData entityMetaData) {
        cache.put(entityClass, entityMetaData);
    }

    public EntityMetaData get(Class<?> entityClass) {
        return cache.get(entityClass);
    }

    public boolean containsKey(Class<?> entityClass) {
        return cache.containsKey(entityClass);
    }
}
