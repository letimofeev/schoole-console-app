package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.dto.EntityMetaData;
import org.foxminded.springcourse.consoleapp.manager.EntityMetaDataManager;
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
        String queryTemplate = "INSERT INTO %s (%s) VALUES (%s);";
        EntityMetaData entityMetaData = metaDataManager.getMetaData(entity.getClass());
        return String.format(queryTemplate,
                entityMetaData.getTable(),
                getUpdatableColumns(entityMetaData),
                getBindParametersForInsert(entityMetaData));
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

    @Override
    public String buildFindAllQuery(Class<T> entityClass) {
        String queryTemplate = "SELECT %s FROM %s;";
        EntityMetaData entityMetaData = metaDataManager.getMetaData(entityClass);
        return String.format(queryTemplate,
                getColumns(entityMetaData),
                entityMetaData.getTable());
    }

    @Override
    public String buildUpdateQuery(T entity) {
        String queryTemplate = "UPDATE %s SET %s WHERE %s = ?";
        EntityMetaData entityMetaData = metaDataManager.getMetaData(entity.getClass());
        return String.format(queryTemplate,
                entityMetaData.getTable(),
                getBindParametersForUpdate(entityMetaData),
                entityMetaData.getIdColumn());
    }

    @Override
    public String buildDeleteByIdQuery(ID id, Class<T> entityClass) {
        String queryTemplate = "DELETE FROM %s WHERE %s = ?";
        EntityMetaData entityMetaData = metaDataManager.getMetaData(entityClass);
        return String.format(queryTemplate,
                entityMetaData.getTable(),
                entityMetaData.getIdColumn());
    }

    private String getUpdatableColumns(EntityMetaData entityMetaData) {
        return String.join(", ", entityMetaData.getUpdatableColumns());
    }

    private String getColumns(EntityMetaData entityMetaData) {
        return String.join(", ", entityMetaData.getColumns());
    }

    private String getBindParametersForInsert(EntityMetaData entityMetaData) {
        return entityMetaData.getUpdatableColumns().stream()
                .map(column -> "?")
                .collect(Collectors.joining(", "));
    }

    private String getBindParametersForUpdate(EntityMetaData entityMetaData) {
        return entityMetaData.getUpdatableColumns().stream()
                .map(column -> column + " = ?")
                .collect(Collectors.joining(", "));
    }
}
