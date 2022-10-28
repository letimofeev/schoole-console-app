package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.manager.EntityMetaDataManager;
import org.foxminded.springcourse.consoleapp.model.EntityMetaData;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CrudQueryBuilderPostgres<T, ID> implements CrudQueryBuilder<T, ID> {

    private final EntityMetaDataManager metaDataManager;

    public CrudQueryBuilderPostgres(EntityMetaDataManager metaDataManager) {
        this.metaDataManager = metaDataManager;
    }

    @Override
    public String buildSaveQuery(T entity) {
        String queryTemplate = "INSERT INTO %s (%s) VALUES (%s) RETURNING %s;";
        EntityMetaData entityMetaData = metaDataManager.getMetaData(entity.getClass());
        return String.format(queryTemplate,
                entityMetaData.getTable(),
                getUpdatableColumns(entityMetaData),
                getBindParameters(entityMetaData),
                entityMetaData.getIdColumn());
    }

    @Override
    public String buildFindByIdQuery(ID id, Class<T> entityClass) {
        String queryTemplate = "SELECT %s FROM %s WHERE %s = ?;";
        EntityMetaData entityMetaData = metaDataManager.getMetaData(entityClass);
        return String.format(queryTemplate,
                getColumns(entityMetaData),
                entityMetaData.getTable(),
                entityMetaData.getIdColumn());
    }

    private String getUpdatableColumns(EntityMetaData entityMetaData) {
        return String.join(", ", entityMetaData.getUpdatableColumns());
    }

    private String getColumns(EntityMetaData entityMetaData) {
        return String.join(", ", entityMetaData.getColumns());
    }

    private String getBindParameters(EntityMetaData entityMetaData) {
        return entityMetaData.getUpdatableColumns().stream()
                .map(column -> "?")
                .collect(Collectors.joining(", "));
    }
}
