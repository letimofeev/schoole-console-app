package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.exception.DaoException;
import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.foxminded.springcourse.consoleapp.service.EntityDataMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GroupDao extends AbstractCrudDao<Group, Integer> {

    public GroupDao(ConnectionConfig connectionConfig,
                    CrudQueryBuilder<Group, Integer> queryBuilder,
                    EntityDataMapper<Group> dataMapper) {
        super(connectionConfig, queryBuilder, dataMapper);
    }

    public List<Group> findAllWithStudentCountLessThanEqual(int studentCount) {
        String sql = "SELECT groups.group_id, group_name\n" +
                "FROM groups\n" +
                "JOIN students s on groups.group_id = s.group_id\n" +
                "GROUP BY groups.group_id, group_name\n" +
                "HAVING count(*) >= ?";
        return genericExecuteQuery(sql, statement -> bindStudentCount(statement, studentCount),
                resultSet -> dataMapper.collectEntities(Group.class, resultSet));
    }

    private void bindStudentCount(PreparedStatement statement, int studentCount) {
        try {
            statement.setInt(1, studentCount);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
