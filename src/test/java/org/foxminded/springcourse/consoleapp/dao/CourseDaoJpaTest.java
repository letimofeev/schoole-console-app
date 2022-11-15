package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Course;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Testcontainers
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
class CourseDaoJpaTest {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private EntityManager entityManager;

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
    void find_shouldReturnPresentOptional_whenCourseExists() {
        Course expected = new Course(1000, "Doggy", "Ok");
        Course actual = courseDao.find(expected).get();

        assertEquals(expected, actual);
    }

    @Test
    void find_shouldReturnEmptyOptional_whenCourseNotExists() {
        Course course = new Course();
        course.setCourseId(100);

        Optional<Course> actual = courseDao.find(course);

        assertTrue(actual.isEmpty());
    }

    @Sql(statements = "INSERT INTO courses VALUES (1111, 'Kitten', 'Ko')")
    @Test
    void update_shouldUpdate_whenInputIsCourse() {
        Course expected = new Course(1111, "Doggy", "Ok");

        courseDao.update(expected);

        Course actual = entityManager.find(Course.class, 1111);

        assertEquals(expected, actual);
    }

    @Sql(statements = "INSERT INTO courses VALUES (1112, 'L', 'G')")
    @Test
    void delete_shouldDelete_whenInputIsCourse() {
        Course course = new Course();
        course.setCourseId(1112);
        courseDao.delete(course);

        Course actual = entityManager.find(Course.class, 1112);

        assertNull(actual);
    }
}
