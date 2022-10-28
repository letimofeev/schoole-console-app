package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.annotation.Column;
import org.foxminded.springcourse.consoleapp.annotation.Id;
import org.foxminded.springcourse.consoleapp.annotation.Table;
import org.foxminded.springcourse.consoleapp.model.EntityMetaData;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class EntityMetaDataExtractor {

    public EntityMetaData getMetaData(Class<?> entityClass) {
        List<String> idColumns = new ArrayList<>();
        List<String> updatableColumns = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                columns.add(columnAnnotation.name());
                Id idAnnotation = field.getAnnotation(Id.class);
                if (idAnnotation == null || !idAnnotation.autogenerated()) {
                    updatableColumns.add(columnAnnotation.name());
                }
                if (idAnnotation != null) {
                    idColumns.add(columnAnnotation.name());
                }
            }
        }
        checkIdColumnNamesCount(idColumns);
        String id = idColumns.get(0);
        String table = getTableName(entityClass);
        return new EntityMetaData(table, id, columns, updatableColumns);
    }


    private String getTableName(Class<?> entityClass) {
        Table table = entityClass.getAnnotation(Table.class);
        if (table == null) {
            String message = String.format("Entity %s must be annotated with @%s",
                    entityClass.getSimpleName(), Table.class.getSimpleName());
            throw new IllegalArgumentException(message);
        }
        return table.name();
    }

    private void checkIdColumnNamesCount(List<String> idColumnNames) {
        if (idColumnNames.size() == 0) {
            throw new IllegalArgumentException("Id column must be specified");
        } else if (idColumnNames.size() > 1) {
            throw new IllegalArgumentException("Multiple Id specification found");
        }
    }
}
