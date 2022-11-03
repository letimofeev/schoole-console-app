package org.foxminded.springcourse.consoleapp.manager;

import org.foxminded.springcourse.consoleapp.dto.EntityMetaData;
import org.foxminded.springcourse.consoleapp.dto.EntityMetaDataCache;
import org.foxminded.springcourse.consoleapp.service.EntityMetaDataExtractor;
import org.springframework.stereotype.Component;

@Component
public class EntityMetaDataManager {

    private final EntityMetaDataCache metaDataCache;
    private final EntityMetaDataExtractor metaDataExtractor;

    public EntityMetaDataManager(EntityMetaDataCache metaDataCache,
                                 EntityMetaDataExtractor metaDataExtractor) {
        this.metaDataCache = metaDataCache;
        this.metaDataExtractor = metaDataExtractor;
    }

    public EntityMetaData getMetaData(Class<?> entityClass) {
        if (metaDataCache.containsKey(entityClass)) {
            return metaDataCache.get(entityClass);
        } else {
            EntityMetaData entityMetaData = metaDataExtractor.getMetaData(entityClass);
            metaDataCache.put(entityClass, entityMetaData);
            return entityMetaData;
        }
    }
}
