package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Group;
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
class GroupRowMapperTest {

    private final GroupRowMapper rowMapper = new GroupRowMapper();

    @Mock
    private ResultSet resultSet;

    @BeforeAll
    void setUp() throws SQLException {
        openMocks(this);

        when(resultSet.getInt("group_id")).thenReturn(901);
        when(resultSet.getString("group_name")).thenReturn("Name");
    }

    @Test
    void mapRow_shouldReturnMapped_whenInputIsResultSet() throws SQLException {
        Group expected = new Group(901, "Name");
        Group actual = rowMapper.mapRow(resultSet, 11);

        assertEquals(expected, actual);
    }
}