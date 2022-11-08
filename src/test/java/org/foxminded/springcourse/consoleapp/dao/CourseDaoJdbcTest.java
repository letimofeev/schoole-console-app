package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Course;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Testcontainers
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
class CourseDaoJdbcTest {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final CourseRowMapper rowMapper = new CourseRowMapper();

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:13.3")
            .withDatabaseName("school-test")
            .withInitScript("courses_test_tables.sql");

    @BeforeAll
    static void setUp() {
        container.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.driver-class-name", container::getDriverClassName);
    }

    @AfterAll
    static void tearDown() {
        container.stop();
    }

    @Test
    void save_shouldInjectId_whenStudentSaved() {
        Course course = new Course("Name", "D");
        courseDao.save(course);

        int unexpectedId = 0;
        int actualId = course.getCourseId();

        assertNotEquals(unexpectedId, actualId);
    }

    @Sql("classpath:courses_data.sql")
    @Test
    void findAll_shouldReturnExpectedRowsNumber_whenCoursesExist() {
        int expectedSize = 8;
        int actualSize = courseDao.findAll().size();

        assertEquals(expectedSize, actualSize);
    }

    @Sql(statements = "INSERT INTO courses VALUES (1000, 'Doggy', 'Ok')")
    @Test
    void findById_shouldReturnPresentOptional_whenCourseExists() {
        Course expected = new Course(1000, "Doggy", "Ok");
        Course actual = courseDao.findById(1000).get();

        assertEquals(expected, actual);
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenCourseExists() {
        Optional<Course> actual = courseDao.findById(1000);
        assertTrue(actual.isEmpty());
    }

    @Sql(statements = "INSERT INTO courses VALUES (1111, 'Kitten', 'Ko')")
    @Test
    void update_shouldUpdate_whenInputIsId() {
        Course expected = new Course(1111, "Doggy", "Ok");

        courseDao.update(expected);

        String query = "SELECT * FROM courses WHERE course_id = ?";
        Course actual = jdbcTemplate.query(query, rowMapper, 1111).get(0);

        assertEquals(expected, actual);
    }

    @Sql(statements = "INSERT INTO courses VALUES (1112, 'L', 'G')")
    @Test
    void deleteById_shouldDelete_whenInputIsId() {
        courseDao.deleteById(1112);

        String query = "SELECT * FROM courses WHERE course_id = ?";
        List<Course> actual = jdbcTemplate.query(query, rowMapper, 1112);

        assertTrue(actual.isEmpty());
    }
}
