package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(this.getClass());

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

        log.debug("Course saved in table 'courses'. Values: course_id = {}, course_name = {}, course_description = {}",
                course.getCourseId(), course.getCourseName(), course.getCourseDescription());
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
            log.warn("Course with id = {} not found in table 'courses'", id);
            return Optional.empty();
        }
    }

    @Override
    public void update(Course course) {
        int id = course.getCourseId();
        String courseName = course.getCourseName();
        String courseDescription = course.getCourseDescription();
        jdbcTemplate.update(UPDATE_BY_ID, courseName, courseDescription, id);

        log.debug("Course with id = {} updated in table 'courses', new values: course_name = {}, " +
                        "course_description = {}", id, courseName, courseDescription);
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
        log.debug("Course with id = {} deleted from table 'courses'", id);
    }
}
