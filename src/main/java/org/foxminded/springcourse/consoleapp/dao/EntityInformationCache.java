package org.foxminded.springcourse.consoleapp.dao;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EntityInformationCache {

    private final Map<Class<?>, EntityInformation> cache = new ConcurrentHashMap<>();

    public void put(Class<?> entityClass, EntityInformation entityInformation) {
        cache.put(entityClass, entityInformation);
    }

    public EntityInformation get(Class<?> entityClass) {
        return cache.get(entityClass);
    }

    public boolean containsKey(Class<?> entityClass) {
        return cache.containsKey(entityClass);
    }
}
