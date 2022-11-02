package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.annotation.Column;
import org.foxminded.springcourse.consoleapp.annotation.Id;
import org.foxminded.springcourse.consoleapp.annotation.Table;
import org.foxminded.springcourse.consoleapp.dto.EntityMetaData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityMetaDataExtractorTest {

    private final EntityMetaDataExtractor metaDataExtractor = new EntityMetaDataExtractor();

    private final Object entity = new @Table(name = "table") Object() {

        @Id(autogenerated = true)
        @Column(name = "column_id")
        private int id;

        @Column(name = "column_1")
        private String column1;

        @Column(name = "column_2")
        private String column2;
    };

    @Test
    void getMetaData_shouldReturnEntityMetaData_whenInputIsEntity() {
        List<String> expectedColumns = List.of("column_id", "column_1", "column_2");
        List<String> expectedUpdatableColumns = List.of("column_1", "column_2");
        EntityMetaData actual = metaDataExtractor.getMetaData(entity.getClass());

        assertEquals(actual.getTable(), "table");
        assertEquals(actual.getIdColumn(), "column_id");

        List<String> actualColumns = actual.getColumns();
        assertTrue((expectedColumns.size() == actualColumns.size())
                && actualColumns.containsAll(expectedColumns)
                && expectedColumns.containsAll(actualColumns));

        List<String> actualUpdatableColumns = actual.getUpdatableColumns();
        assertTrue((expectedUpdatableColumns.size() == actualUpdatableColumns.size())
                && actualUpdatableColumns.containsAll(expectedUpdatableColumns)
                && expectedUpdatableColumns.containsAll(actualUpdatableColumns));
    }
}
