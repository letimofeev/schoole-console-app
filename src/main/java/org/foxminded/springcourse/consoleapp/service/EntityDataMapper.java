package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.annotation.Column;
import org.foxminded.springcourse.consoleapp.annotation.Id;
import org.foxminded.springcourse.consoleapp.dto.EntityMetaData;
import org.foxminded.springcourse.consoleapp.exception.EntityDataMapperException;
import org.foxminded.springcourse.consoleapp.manager.EntityMetaDataManager;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EntityDataMapper<T> {

    private final EntityMetaDataManager metaDataManager;

    public EntityDataMapper(EntityMetaDataManager metaDataManager) {
        this.metaDataManager = metaDataManager;
    }

    public int bindUpdatableColumns(PreparedStatement statement, T entity) {
        EntityMetaData entityMetaData = metaDataManager.getMetaData(entity.getClass());
        List<String> updatableColumns = entityMetaData.getUpdatableColumns();
        int bindValuesNumber = 0;
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
                            bindValuesNumber++;
                        } catch (SQLException | IllegalAccessException e) {
                            throw new EntityDataMapperException(e);
                        }
                    }
                }
            }
        }
        return bindValuesNumber;
    }

    public void bindIdValue(PreparedStatement statement, T entity, int bindParameterIndex) {
        Object id = getEntityId(entity);
        bindValue(statement, id, bindParameterIndex);
    }

    public void bindValues(PreparedStatement statement, Object... values) {
        for (int i = 1; i <= values.length; i++) {
            Object value = values[i - 1];
            bindValue(statement, value, i);
        }
    }

    public void bindValue(PreparedStatement statement, Object value, int bindParameterIndex) {
        try {
            statement.setObject(bindParameterIndex, value);
        } catch (SQLException e) {
            throw new EntityDataMapperException(e);
        }
    }

    public void getNextAndFillEntity(T entity, ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                fillEntity(entity, resultSet);
            }
        } catch (SQLException e) {
            throw new EntityDataMapperException(e);
        }
    }

    public void fillEntity(T entity, ResultSet resultSet) {
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

    public void fillEntityId(T entity, Object value) {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                if (value instanceof Long) {
                    value = ((Long) value).intValue();
                }
                ReflectionUtils.setField(field, entity, value);
            }
        }

    }

    public List<T> collectEntities(Class<T> entityClass, ResultSet resultSet) {
        List<T> entities = new ArrayList<>();
        try {
            while (resultSet.next()) {
                T entity = entityClass.getConstructor().newInstance();
                fillEntity(entity, resultSet);
                entities.add(entity);
            }
            return entities;
        } catch (Exception e) {
            throw new EntityDataMapperException(e);
        }
    }

    private Set<String> getResultSetColumns(ResultSet resultSet) {
        Set<String> columns = new HashSet<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                columns.add(columnName.toLowerCase());
            }
        } catch (SQLException e) {
            throw new EntityDataMapperException(e);
        }
        return columns;
    }

    private Object getEntityId(T entity) {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                try {
                    return field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new EntityDataMapperException(e);
                }
            }
        }
        throw new IllegalArgumentException("Id column must be specified");
    }
}
