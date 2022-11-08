package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Student;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class StudentDaoJdbc implements StudentDao {

    public static final String FIND_ALL = "SELECT * FROM students";
    public static final String FIND_ALL_BY_COURSE_NAME = "SELECT students.student_id, group_id, first_name, last_name\n" +
            "FROM students\n" +
            "JOIN students_courses sc on students.student_id = sc.student_id\n" +
            "JOIN courses c on c.course_id = sc.course_id\n" +
            "WHERE c.course_name = ?";
    public static final String FIND_BY_ID = "SELECT * FROM students WHERE student_id = ?";
    public static final String UPDATE_BY_ID = "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
    public static final String DELETE_BY_ID = "DELETE FROM students WHERE student_id = ?";
    public static final String ADD_STUDENT_COURSE = "INSERT INTO students_courses (student_id, course_id) VALUES (?, ?)";
    public static final String DELETE_STUDENT_COURSE = "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Student> studentRowMapper = new StudentRowMapper();
    private final SimpleJdbcInsert simpleJdbcInsert;

    public StudentDaoJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("students")
                .usingGeneratedKeyColumns("student_id");
    }

    @Override
    public void save(Student student) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("group_id", student.getGroupId());
        parameters.put("first_name", student.getFirstName());
        parameters.put("last_name", student.getLastName());

        Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
        student.setStudentId(id.intValue());
    }

    @Override
    public List<Student> findAll() {
        return jdbcTemplate.query(FIND_ALL, studentRowMapper);
    }

    @Override
    public List<Student> findAllByCourseName(String courseName) {
        return jdbcTemplate.query(FIND_ALL_BY_COURSE_NAME, studentRowMapper, courseName);

    }

    @Override
    public Optional<Student> findById(int id) {
        try {
            Student student = jdbcTemplate.queryForObject(FIND_BY_ID, studentRowMapper, id);
            return Optional.ofNullable(student);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Student student) {
        int id = student.getStudentId();
        int groupId = student.getGroupId();
        String firstName = student.getFirstName();
        String lastName = student.getLastName();
        jdbcTemplate.update(UPDATE_BY_ID, groupId, firstName, lastName, id);
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public void addStudentCourse(int studentId, int courseId) {
        jdbcTemplate.update(ADD_STUDENT_COURSE, studentId, courseId);
    }

    @Override
    public void deleteStudentCourse(int studentId, int courseId) {
        jdbcTemplate.update(DELETE_STUDENT_COURSE, studentId, courseId);
    }
}
