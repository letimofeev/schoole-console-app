package org.foxminded.springcourse.consoleapp.dao;

import lombok.Getter;

import java.util.List;

@Getter
public class EntityInformation {

    private final String idColumn;
    private final List<String> columns;
    private final List<String> updatableColumns;

    public EntityInformation(String idColumn, List<String> columns, List<String> updatableColumns) {
        this.idColumn = idColumn;
        this.columns = columns;
        this.updatableColumns = updatableColumns;
    }
}
