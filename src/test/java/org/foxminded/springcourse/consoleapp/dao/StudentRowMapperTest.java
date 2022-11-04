package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@TestInstance(PER_CLASS)
class StudentRowMapperTest {

    private final StudentRowMapper rowMapper = new StudentRowMapper();

    @BeforeAll
    void setUp() throws SQLException {
        openMocks(this);

        when(resultSet.getInt("student_id")).thenReturn(1001);
        when(resultSet.getInt("group_id")).thenReturn(722);
        when(resultSet.getString("first_name")).thenReturn("First");
        when(resultSet.getString("last_name")).thenReturn("Last");
    }

    @Mock
    private ResultSet resultSet;

    @Test
    void mapRow_shouldReturnMapped_whenInputIsResultSet() throws SQLException {
        Student expected = new Student(1001, 722, "First", "Last");
        Student actual = rowMapper.mapRow(resultSet, 99999);

        assertEquals(expected, actual);
    }
}
