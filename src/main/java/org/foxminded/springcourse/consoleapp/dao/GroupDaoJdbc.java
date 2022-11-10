package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GroupDaoJdbc implements GroupDao {

    public static final String FIND_ALL = "SELECT * FROM groups";
    public static final String FIND_ALL_WITH_LTE_STUDENTS = "SELECT groups.group_id, group_name\n" +
            "FROM groups\n" +
            "JOIN students s on groups.group_id = s.group_id\n" +
            "GROUP BY groups.group_id, group_name\n" +
            "HAVING count(*) <= ?";
    public static final String FIND_BY_ID = "SELECT * FROM groups WHERE group_id = ?";
    public static final String UPDATE_BY_ID = "UPDATE groups SET group_name = ? WHERE group_id = ?";
    public static final String DELETE_BY_ID = "DELETE FROM groups WHERE group_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Group> groupRowMapper = new GroupRowMapper();
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

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

        log.debug("Group saved in table 'groups', values: group_id = {}, group_name = {}",
                group.getGroupId(), group.getGroupName());
    }

    @Override
    public List<Group> findAll() {
        return jdbcTemplate.query(FIND_ALL, groupRowMapper);
    }

    @Override
    public List<Group> findAllWithStudentCountLessThanEqual(int studentsCount) {
        return jdbcTemplate.query(FIND_ALL_WITH_LTE_STUDENTS, groupRowMapper, studentsCount);
    }

    @Override
    public Optional<Group> findById(int id) {
        try {
            Group group = jdbcTemplate.queryForObject(FIND_BY_ID, groupRowMapper, id);
            return Optional.ofNullable(group);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Group with id = {} not found in table 'groups'", id);
            return Optional.empty();
        }
    }

    @Override
    public void update(Group group) {
        int id = group.getGroupId();
        String groupName = group.getGroupName();
        jdbcTemplate.update(UPDATE_BY_ID, groupName, id);

        log.debug("Group with id = {} updated in table 'groups', new values: group_name = {}",
                id, group.getGroupName());
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
        log.debug("Group with id = {} deleted from table 'groups'", id);
    }
}
