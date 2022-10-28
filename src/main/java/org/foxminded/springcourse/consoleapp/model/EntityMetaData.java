package org.foxminded.springcourse.consoleapp.model;

import lombok.Getter;

import java.util.List;

@Getter
public class EntityMetaData {

    private final String table;
    private final String idColumn;
    private final List<String> columns;
    private final List<String> updatableColumns;

    public EntityMetaData(String table, String idColumn, List<String> columns,
                          List<String> updatableColumns) {
        this.table = table;
        this.idColumn = idColumn;
        this.columns = columns;
        this.updatableColumns = updatableColumns;
    }
}
