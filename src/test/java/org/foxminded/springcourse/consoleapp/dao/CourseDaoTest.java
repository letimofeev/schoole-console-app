package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.manager.EntityMetaDataManager;
import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.model.EntityMetaDataCache;
import org.foxminded.springcourse.consoleapp.service.EntityDataMapper;
import org.foxminded.springcourse.consoleapp.service.EntityMetaDataExtractor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class CourseDaoTest {

    private final ConnectionConfig connectionConfig = new ConnectionConfig("jdbc:h2:mem:school;DB_CLOSE_DELAY=-1", "sa", "");

    private final EntityMetaDataManager metaDataManager = new EntityMetaDataManager(new EntityMetaDataCache(), new EntityMetaDataExtractor());
    private final CrudQueryBuilder<Course, Integer> queryBuilder = new CrudQueryBuilderPostgres<>(metaDataManager);
    private final EntityDataMapper<Course> dataMapper = new EntityDataMapper<>(metaDataManager);

    private CourseDao courseDao;

    @BeforeAll
    void beforeAll() {
        courseDao = new CourseDao(connectionConfig, queryBuilder, dataMapper);
    }

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionConfig.getUrl(), "sa", "")) {
            Statement statement = connection.createStatement();
            statement.execute("create table courses \n" +
                    "(\n" +
                    "  id           integer auto_increment,\n" +
                    "  course_name varchar(20),\n" +
                    "  course_description varchar(20)\n" +
                    ");");
        }
    }

    @Test
    void save_shouldInjectId_whenObjectSaved() throws SQLException {
        Course course = new Course("name", "description");
        courseDao.save(course);

        int expectedId = 1;
        int actualId = course.getId();

        assertEquals(expectedId, actualId);
    }
}