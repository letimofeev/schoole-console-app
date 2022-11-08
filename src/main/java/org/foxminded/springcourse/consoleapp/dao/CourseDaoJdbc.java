package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Course;
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
public class CourseDaoJdbc implements CourseDao {

    public static final String FIND_ALL = "SELECT * FROM courses";
    public static final String FIND_BY_ID = "SELECT * FROM courses WHERE course_id = ?";
    public static final String UPDATE_BY_ID = "UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?";
    public static final String DELETE_BY_ID = "DELETE FROM courses WHERE course_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Course> courseRowMapper = new CourseRowMapper();
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CourseDaoJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("courses")
                .usingGeneratedKeyColumns("course_id");
    }

    @Override
    public void save(Course course) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("course_name", course.getCourseName());
        parameters.put("course_description", course.getCourseDescription());

        Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
        course.setCourseId(id.intValue());
    }

    @Override
    public List<Course> findAll() {
        return jdbcTemplate.query(FIND_ALL, courseRowMapper);
    }

    @Override
    public Optional<Course> findById(int id) {
        try {
            Course course = jdbcTemplate.queryForObject(FIND_BY_ID, courseRowMapper, id);
            return Optional.ofNullable(course);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Course course) {
        int id = course.getCourseId();
        String courseName = course.getCourseName();
        String courseDescription = course.getCourseDescription();
        jdbcTemplate.update(UPDATE_BY_ID, courseName, courseDescription, id);
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
