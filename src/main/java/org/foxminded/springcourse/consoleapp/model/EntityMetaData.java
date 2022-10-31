package org.foxminded.springcourse.consoleapp.model;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityMetaData that = (EntityMetaData) o;

        if (!Objects.equals(table, that.table)) return false;
        if (!Objects.equals(idColumn, that.idColumn)) return false;
        if (!Objects.equals(columns, that.columns)) return false;
        return Objects.equals(updatableColumns, that.updatableColumns);
    }

    @Override
    public int hashCode() {
        int result = table != null ? table.hashCode() : 0;
        result = 31 * result + (idColumn != null ? idColumn.hashCode() : 0);
        result = 31 * result + (columns != null ? columns.hashCode() : 0);
        result = 31 * result + (updatableColumns != null ? updatableColumns.hashCode() : 0);
        return result;
    }
}
