package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.annotation.Column;
import org.foxminded.springcourse.consoleapp.exception.EntityDataMapperException;
import org.foxminded.springcourse.consoleapp.manager.EntityMetaDataManager;
import org.foxminded.springcourse.consoleapp.model.EntityMetaData;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class EntityDataMapper<T, ID> {

    private final EntityMetaDataManager metaDataManager;

    public EntityDataMapper(EntityMetaDataManager metaDataManager) {
        this.metaDataManager = metaDataManager;
    }

    public void bindAllColumns(PreparedStatement statement, T entity) {
        EntityMetaData entityMetaData = metaDataManager.getMetaData(entity.getClass());
        List<String> updatableColumns = entityMetaData.getUpdatableColumns();
        for (int i = 1; i <= updatableColumns.size(); i++) {
            String column = updatableColumns.get(i - 1);
            for (Field field : entity.getClass().getDeclaredFields()) {
                Column annotation = field.getAnnotation(Column.class);
                if (annotation != null) {
                    String fieldColumn = annotation.name();
                    if (column.equals(fieldColumn)) {
                        field.setAccessible(true);
                        try {
                            Object value = field.get(entity);
                            statement.setObject(i, value);
                        } catch (SQLException | IllegalAccessException e) {
                            throw new EntityDataMapperException(e);
                        }
                    }
                }
            }
        }
    }

    public void bindIdColumn(PreparedStatement statement, ID id) {
        bindIdColumn(statement, id, 1);
    }

    public void bindIdColumn(PreparedStatement statement, ID id, int bindParameterIndex) {
        try {
            statement.setObject(bindParameterIndex, id);
        } catch (SQLException e) {
            throw new EntityDataMapperException(e);
        }

    }

    public void takeNextAndBindEntity(T entity, ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                bindEntity(entity, resultSet);
            }
        } catch (SQLException e) {
            throw new EntityDataMapperException(e);
        }
    }

    public void bindEntity(T entity, ResultSet resultSet) {
        Set<String> columns = getResultSetColumns(resultSet);
        for (Field field : entity.getClass().getDeclaredFields()) {
            Column annotation = field.getAnnotation(Column.class);
            if (annotation != null) {
                String columnName = annotation.name();
                if (columns.contains(columnName)) {
                    try {
                        Object value = resultSet.getObject(columnName);
                        field.setAccessible(true);
                        field.set(entity, value);
                    } catch (SQLException | IllegalAccessException e) {
                        throw new EntityDataMapperException(e);
                    }
                }
            }
        }
    }

    private Set<String> getResultSetColumns(ResultSet resultSet) {
        Set<String> columns = new HashSet<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                columns.add(columnName);
            }
        } catch (SQLException e) {
            throw new EntityDataMapperException(e);
        }
        return columns;
    }
}
