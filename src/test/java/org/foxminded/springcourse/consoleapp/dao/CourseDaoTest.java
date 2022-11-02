package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.manager.EntityMetaDataManager;
import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.model.EntityMetaDataCache;
import org.foxminded.springcourse.consoleapp.service.EntityDataMapper;
import org.foxminded.springcourse.consoleapp.service.EntityMetaDataExtractor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseDaoTest {

    private final ConnectionConfig connectionConfig = new ConnectionConfig("jdbc:h2:mem:school;DB_CLOSE_DELAY=-1", "sa", "");

    private final EntityMetaDataManager metaDataManager = new EntityMetaDataManager(new EntityMetaDataCache(), new EntityMetaDataExtractor());
    private final CrudQueryBuilder<Course, Integer> queryBuilder = new CrudQueryBuilderPostgres<>(metaDataManager);
    private final EntityDataMapper<Course> dataMapper = new EntityDataMapper<>(metaDataManager);

    private final CourseDao courseDao = new CourseDao(connectionConfig, queryBuilder, dataMapper);

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionConfig.getUrl(), "sa", "")) {
            Statement statement = connection.createStatement();
            statement.execute("create table courses \n" +
                    "(\n" +
                    "  course_id integer auto_increment,\n" +
                    "  course_name varchar(20),\n" +
                    "  course_description varchar(20)\n" +
                    ");");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionConfig.getUrl(), "sa", "")) {
            Statement statement = connection.createStatement();
            statement.execute("drop table if exists courses;");
        }
    }

    @Test
    void save_shouldInjectId_whenCourseSaved() {
        Course course = new Course("name", "description");
        courseDao.save(course);

        int expectedId = 1;
        int actualId = course.getId();

        assertEquals(expectedId, actualId);
    }

    @Test
    void findById_shouldReturnEmpty_whenCourseDoesNotExist() {
        int id = -1;

        Optional<Course> actual = courseDao.findById(id, Course.class);

        assertTrue(actual.isEmpty());
    }

    @Test
    void findById_shouldReturnActual_whenCourseExists() {
        Course course = new Course("name", "desc");
        courseDao.save(course);

        Course expected = new Course(1, "name", "desc");
        Optional<Course> actualOptional = courseDao.findById(course.getId(), Course.class);

        assertTrue(actualOptional.isPresent());
        assertEquals(course, actualOptional.get());
    }

    @Test
    void findAll_shouldReturnEmpty_whenCoursesDoNotExist() {
        List<Course> actual = courseDao.findAll(Course.class);

        assertTrue(actual.isEmpty());
    }

    @Test
    void findAll_shouldReturnActual_whenCoursesExist() {
        for (int i = 1; i <= 100; i++) {
            Course course = new Course("name" + i, "desc" + i);
            courseDao.save(course);
        }

        List<Course> actual = courseDao.findAll(Course.class);

        for (int i = 0; i < 100; i++) {
            int id = i + 1;
            Course expectedCourse = new Course(id, "name" + id, "desc" + id);
            Course actualCourse = actual.get(i);
            assertEquals(expectedCourse, actualCourse);
        }
    }

    @Test
    void update_shouldUpdate_whenCourseUpdated() {
        Course course = new Course("name", "desc");
        courseDao.save(course);

        course.setCourseName("updatedName");
        course.setCourseDescription("updatedDesc");
        courseDao.update(course);

        Course expected = new Course(1, "updatedName", "updatedDesc");
        Optional<Course> actualOptional = courseDao.findById(course.getId(), Course.class);

        assertTrue(actualOptional.isPresent());
        assertEquals(expected, actualOptional.get());
    }

    @Test
    void deleteById_shouldDelete_whenCourseExists() {
        Course course = new Course("meth", "varim meth");
        courseDao.save(course);

        assertTrue(courseDao.findById(1, Course.class).isPresent());

        courseDao.deleteById(1, Course.class);

        assertTrue(courseDao.findById(1, Course.class).isEmpty());
    }
}
