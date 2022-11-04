package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Group;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    private final SimpleJdbcInsert simpleJdbcInsert;

    public GroupDaoJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("groups")
                .usingGeneratedKeyColumns("group_id");
    }

    @Override
    public void save(Group group) {
        Map<String, Object> parameters = Map.of("group_name", group.getGroupName());
        Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
        group.setGroupId(id.intValue());
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
