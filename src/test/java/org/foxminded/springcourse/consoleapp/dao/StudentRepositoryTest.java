package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Student;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Testcontainers
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EntityManager entityManager;

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
        studentRepository.save(student);

        int unexpectedId = 0;
        int actualId = student.getStudentId();

        assertNotEquals(unexpectedId, actualId);
    }

    @Test
    void findAllByCourseName_shouldReturnEmpty_whenStudentsDoNotExist() {
        List<Student> actual = studentRepository.findAllByCourseName("course");

        assertTrue(actual.isEmpty());
    }

    @Sql("classpath:students_courses_data.sql")
    @Test
    void findAllByCourseName_shouldReturnActualRowsNumber_whenStudentsExist() {
        int expectedSize = 3;
        int actualSize = studentRepository.findAllByCourseName("course3").size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void findAll_shouldReturnEmpty_whenStudentsDoNotExist() {
        List<Student> actual = studentRepository.findAll();

        assertTrue(actual.isEmpty());
    }

    @Sql("classpath:students_data.sql")
    @Test
    void findAll_shouldReturnActualRowsNumber_whenStudentsExist() {
        int expectedSize = 10;
        int actualSize = studentRepository.findAll().size();

        assertEquals(expectedSize, actualSize);
    }

    @Sql(statements = "INSERT INTO students VALUES (12, 10, 'Enzo', 'Ferrari')")
    @Test
    void findById_shouldReturnPresentOptional_whenStudentExists() {
        Student expected = new Student(12, 10, "Enzo", "Ferrari");
        Student actual = studentRepository.findById(12).get();

        assertEquals(expected, actual);
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenStudentNotExists() {
        Optional<Student> actual = studentRepository.findById(1000);

        assertTrue(actual.isEmpty());
    }

    @Sql("classpath:students_data.sql")
    @Test
    void delete_shouldDelete_whenStudentExists() {
        Student student = new Student();
        student.setStudentId(1);

        studentRepository.delete(student);

        Student actual = entityManager.find(Student.class, 1);

        assertNull(actual);
    }

    @Test
    void addStudentCourse_shouldAddStudentToCourse_whenInputIsStudentAndCourseIds() {
        studentRepository.addStudentCourse(4, 3);

        String query = "SELECT * FROM students_courses WHERE student_id = 4 AND course_id = 3";
        List<?> students = entityManager.createNativeQuery(query).getResultList();

        assertFalse(students.isEmpty());
    }

    @Sql("classpath:students_courses_data.sql")
    @Test
    void deleteStudentCourse_shouldDeleteStudentFromCourse_whenInputIsStudentAndCourseIds() {
        studentRepository.deleteStudentCourse(1, 2);

        String query = "SELECT * FROM students_courses WHERE student_id = 1 AND course_id = 2";
        List<?> students = entityManager.createNativeQuery(query).getResultList();

        assertTrue(students.isEmpty());
    }
}
