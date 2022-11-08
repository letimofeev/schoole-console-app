package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Course;
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
class CourseRowMapperTest {

    @Mock
    private ResultSet resultSet;

    private final CourseRowMapper rowMapper = new CourseRowMapper();

    @BeforeAll
    void setUp() throws SQLException {
        openMocks(this);

        when(resultSet.getInt("course_id")).thenReturn(999);
        when(resultSet.getString("course_name")).thenReturn("Do");
        when(resultSet.getString("course_description")).thenReturn("Re");
    }

    @Test
    void mapRow_shouldReturnMapped_whenInputIsResultSet() throws SQLException {
        Course expected = new Course(999, "Do", "Re");
        Course actual = rowMapper.mapRow(resultSet, 99);

        assertEquals(expected, actual);
    }
}
