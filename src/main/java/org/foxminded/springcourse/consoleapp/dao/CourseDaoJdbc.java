package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Course;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CourseDaoJdbc implements CourseDao {

    public static final String FIND_ALL = "SELECT * FROM courses";

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
        parameters.put("course_desc", course.getCourseDescription());

        Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
        course.setCourseId(id.intValue());
    }

    @Override
    public List<Course> findAll() {
        return jdbcTemplate.query(FIND_ALL, courseRowMapper);
    }

}
