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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Testcontainers
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
class CourseDaoJdbcTest {

    @Autowired
    private CourseDao courseDao;

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

    @Sql("classpath:courses_data.sql")
    @Test
    void findAll_shouldReturnExpected_whenCoursesExist() {
        List<Course> expected = List.of(new Course(1, "math", "math_desc"),
                new Course(2, "meth", "meth_desc"),
                new Course(3, "informatics", "informatics_desc"),
                new Course(4, "computer_science", "computer_science_desc"),
                new Course(5, "memelogy", "memelogy_desc"),
                new Course(6, "religion", "religion_desc"),
                new Course(7, "functional_analysis", "functional_analysis_desc"),
                new Course(8, "machine_learning", "machine_learning_desc"));

        List<Course> actual = courseDao.findAll();

        assertEquals(expected, actual);
    }
}
