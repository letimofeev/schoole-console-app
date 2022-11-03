package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.dto.EntityMetaDataCache;
import org.foxminded.springcourse.consoleapp.manager.EntityMetaDataManager;
import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.foxminded.springcourse.consoleapp.service.EntityDataMapper;
import org.foxminded.springcourse.consoleapp.service.EntityMetaDataExtractor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class StudentDaoTest {

    private final ConnectionConfig connectionConfig = new ConnectionConfig("jdbc:h2:mem:school;DB_CLOSE_DELAY=-1", "sa", "");

    private final EntityMetaDataManager metaDataManager = new EntityMetaDataManager(new EntityMetaDataCache(), new EntityMetaDataExtractor());
    private final CrudQueryBuilder<Student, Integer> queryBuilder = new CrudQueryBuilderPostgres<>(metaDataManager);
    private final EntityDataMapper<Student> dataMapper = new EntityDataMapper<>(metaDataManager);

    private final StudentDao studentDao = new StudentDao(connectionConfig, queryBuilder, dataMapper);

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionConfig.getUrl(), "sa", "")) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE students\n" +
                    "(\n" +
                    "    student_id INTEGER AUTO_INCREMENT,\n" +
                    "    group_id   INTEGER,\n" +
                    "    first_name VARCHAR(20),\n" +
                    "    last_name  VARCHAR(20)\n" +
                    ");");
            statement.execute("CREATE TABLE courses\n" +
                    "(\n" +
                    "    course_id          INTEGER AUTO_INCREMENT,\n" +
                    "    course_name        VARCHAR(20),\n" +
                    "    course_description VARCHAR(20)\n" +
                    ");");
            statement.execute("CREATE TABLE students_courses\n" +
                    "(\n" +
                    "    student_id INTEGER,\n" +
                    "    course_id  INTEGER\n" +
                    ");");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionConfig.getUrl(), "sa", "")) {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS students;");
            statement.execute("DROP TABLE IF EXISTS courses;");
            statement.execute("DROP TABLE IF EXISTS students_courses;");
        }
    }

    @Test
    void save_shouldInjectId_whenStudentSaved() {
        Student student = new Student(1, "FirstName", "LastName");
        studentDao.save(student);

        int expectedId = 1;
        int actualId = student.getId();

        assertEquals(expectedId, actualId);
    }

    @Test
    void findById_shouldReturnEmpty_whenStudentDoesNotExist() {
        int id = -1;

        Optional<Student> actual = studentDao.findById(id, Student.class);

        assertTrue(actual.isEmpty());
    }

    @Test
    void findById_shouldReturnActual_whenStudentExists() {
        Student student = new Student(1, "FirstName", "LastName");

        studentDao.save(student);

        Student expected = new Student(1, 1, "FirstName", "LastName");
        Optional<Student> actualOptional = studentDao.findById(student.getId(), Student.class);

        assertEquals(expected, actualOptional.get());
    }

    @Test
    void findAll_shouldReturnEmpty_whenStudentsDoNotExist() {
        List<Student> actual = studentDao.findAll(Student.class);

        assertTrue(actual.isEmpty());
    }

    @Test
    void findAll_shouldReturnActual_whenStudentsExist() {
        for (int i = 1; i <= 100; i++) {
            Student student = new Student(1, "FirstName" + i, "LastName" + i);
            studentDao.save(student);
        }

        List<Student> actual = studentDao.findAll(Student.class);

        for (int i = 0; i < 100; i++) {
            int id = i + 1;
            Student expectedStudent = new Student(id, 1, "FirstName" + id, "LastName" + id);
            Student actualStudent = actual.get(i);
            assertEquals(expectedStudent, actualStudent);
        }
    }

    @Test
    void update_shouldUpdate_whenStudentUpdated() {
        Student student = new Student(1, "FirstName", "LastName");
        studentDao.save(student);

        student.setFirstName("updatedName");
        studentDao.update(student);

        Student expected = new Student(1, 1, "updatedName", "LastName");
        Optional<Student> actualOptional = studentDao.findById(student.getId(), Student.class);

        assertEquals(expected, actualOptional.get());
    }

    @Test
    void deleteById_shouldDelete_whenStudentExists() {
        Student student = new Student(1, "FirstName", "LastName");
        studentDao.save(student);

        studentDao.deleteById(1, Student.class);

        assertTrue(studentDao.findById(1, Student.class).isEmpty());
    }

    @Test
    void addStudentCourse_shouldAddStudentToCourse_whenInputIsIds() throws SQLException {
        studentDao.addStudentCourse(10, 11);

        try (Connection connection = DriverManager.getConnection(connectionConfig.getUrl(), "sa", "")) {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM students_courses");
            ResultSet resultSet = statement.getResultSet();

            resultSet.next();

            List<Integer> expected = List.of(10, 11);
            List<Integer> actual = List.of(resultSet.getInt(1), resultSet.getInt(2));

            assertEquals(expected, actual);
        }
    }

    @Test
    void deleteStudentCourse() throws SQLException {
        studentDao.addStudentCourse(1, 2);

        studentDao.deleteStudentCourse(1, 2);

        try (Connection connection = DriverManager.getConnection(connectionConfig.getUrl(), "sa", "")) {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM students_courses");
            ResultSet resultSet = statement.getResultSet();

            assertFalse(resultSet.next());
        }
    }

    @Test
    void findAllByCourseName_shouldReturnAllStudentsByCourseName_whenInputIsCourseName() throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionConfig.getUrl(), "sa", "")) {
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO courses (course_name, course_description)\n" +
                    "VALUES ('course1', 'course1desc'),\n" +
                    "       ('course2', 'course2desc'),\n" +
                    "       ('course3', 'course3desc'),\n" +
                    "       ('course4', 'course4desc'),\n" +
                    "       ('course5', 'course5desc'),\n" +
                    "       ('course6', 'course6desc');");
            statement.execute("INSERT INTO students (group_id, first_name, last_name)\n" +
                    "VALUES (1, 'first_name1', 'last_name1'),\n" +
                    "       (2, 'first_name2', 'last_name2'),\n" +
                    "       (3, 'first_name3', 'last_name3'),\n" +
                    "       (1, 'first_name4', 'last_name4'),\n" +
                    "       (1, 'first_name4', 'last_name4'),\n" +
                    "       (1, 'first_name5', 'last_name5'),\n" +
                    "       (2, 'first_name6', 'last_name6'),\n" +
                    "       (3, 'first_name7', 'last_name7');\n" +
                    "\n" +
                    "INSERT INTO students_courses (student_id, course_id)\n" +
                    "VALUES (1, 2),\n" +
                    "       (1, 3),\n" +
                    "       (2, 2),\n" +
                    "       (2, 1),\n" +
                    "       (3, 3),\n" +
                    "       (2, 3);");
        }

        List<Student> expected = List.of(new Student(1, 1, "first_name1", "last_name1"),
                new Student(2, 2, "first_name2", "last_name2"),
                new Student(3, 3, "first_name3", "last_name3"));
        List<Student> actual = studentDao.findAllByCourseName("course3").stream()
                .sorted(Comparator.comparingInt(Student::getId)).collect(Collectors.toList());

        assertEquals(expected, actual);
    }
}
