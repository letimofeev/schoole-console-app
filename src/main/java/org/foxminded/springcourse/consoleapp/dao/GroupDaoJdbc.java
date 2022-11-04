package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Group;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupDaoJdbc implements GroupDao {

    public static final String FIND_ALL = "SELECT * FROM groups";
    public static final String FIND_ALL_WITH_LTE_STUDENTS = "SELECT groups.group_id, group_name\n" +
            "FROM groups\n" +
            "JOIN students s on groups.group_id = s.group_id\n" +
            "GROUP BY groups.group_id, group_name\n" +
            "HAVING count(*) <= ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Group> groupRowMapper = new GroupRowMapper();

    public GroupDaoJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Group> findAll() {
        return jdbcTemplate.query(FIND_ALL, groupRowMapper);
    }

    @Override
    public List<Group> findAllWithStudentCountLessThanEqual(int studentCount) {
        return jdbcTemplate.query(FIND_ALL_WITH_LTE_STUDENTS, groupRowMapper, studentCount);
    }
}
