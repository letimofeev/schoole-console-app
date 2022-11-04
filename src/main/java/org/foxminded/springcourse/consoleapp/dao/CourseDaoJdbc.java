package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Course;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDaoJdbc implements CourseDao {

    public static final String FIND_ALL = "SELECT * FROM courses";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Course> courseRowMapper = new CourseRowMapper();

    public CourseDaoJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Course> findAll() {
        return jdbcTemplate.query(FIND_ALL, courseRowMapper);
    }

}
