package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Student;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Testcontainers
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
class StudentDaoJdbcTest {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Student> rowMapper = new StudentRowMapper();

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:13.3")
            .withDatabaseName("school-test")
            .withInitScript("students_test_tables.sql");

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
        Student student = new Student(1, "FirstName", "LastName");
        studentDao.save(student);

        int unexpectedId = 0;
        int actualId = student.getStudentId();

        assertNotEquals(unexpectedId, actualId);
    }

    @Test
    void findAllByCourseName_shouldReturnEmpty_whenStudentsDoNotExist() {
        List<Student> actual = studentDao.findAllByCourseName("course");

        assertTrue(actual.isEmpty());
    }

    @Sql("classpath:students_courses_data.sql")
    @Test
    void findAllByCourseName_shouldReturnActual_whenStudentsExist() {
        int expectedSize = 3;
        int actualSize = studentDao.findAllByCourseName("course3").size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void findAll_shouldReturnEmpty_whenStudentsDoNotExist() {
        List<Student> actual = studentDao.findAll();

        assertTrue(actual.isEmpty());
    }

    @Sql("classpath:students_data.sql")
    @Test
    void findAll_shouldReturnActual_whenStudentsExist() {
        int expectedSize = 10;
        int actualSize = studentDao.findAll().size();

        assertEquals(expectedSize, actualSize);
    }

    @Sql("classpath:students_data.sql")
    @Test
    void deleteById_shouldDelete_whenStudentExists() {
        studentDao.deleteById(1);

        String query = "SELECT * FROM students WHERE student_id = 1";
        List<Student> allById1 = jdbcTemplate.query(query, rowMapper);

        assertTrue(allById1.isEmpty());
    }

    @Test
    void addStudentCourse() {
        studentDao.addStudentCourse(4, 3);

        String query = "SELECT * FROM students_courses WHERE student_id = 4 AND course_id = 3";
        List<Map<String, Object>> students = jdbcTemplate.queryForList(query);

        assertFalse(students.isEmpty());
    }

    @Sql("classpath:students_courses_data.sql")
    @Test
    void deleteStudentCourse() {
        studentDao.deleteStudentCourse(1, 2);

        String query = "SELECT * FROM students_courses WHERE student_id = 1 AND course_id = 2";
        List<Map<String, Object>> students = jdbcTemplate.queryForList(query);

        assertTrue(students.isEmpty());
    }
}
