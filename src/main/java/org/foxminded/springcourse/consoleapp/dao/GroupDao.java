package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.exception.DaoException;
import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GroupDao extends AbstractCrudDao<Group, Integer> {

    private static final String GROUP_ID_COLUMN = "group_id";
    private static final String GROUP_NAME_COLUMN = "group_name";

    public GroupDao(ConnectionConfig connectionConfig, CrudQueryBuilder<Group> queryBuilder,
                    EntityDataMapper<Group> dataBinder) {
        super(connectionConfig, queryBuilder, dataBinder);
    }

    public List<Group> findAllWithStudentCountLessThanEqual(int studentCount) {
        String sql = "SELECT groups.group_id, group_name\n" +
                "FROM groups\n" +
                "JOIN students s on groups.group_id = s.group_id\n" +
                "GROUP BY groups.group_id, group_name\n" +
                "HAVING count(*) >= ?";
        return genericExecuteQuery(sql, statement -> bindStudentCount(statement, studentCount),
                this::parseGroups);
    }

    private Void bindStudentCount(PreparedStatement statement, int studentCount) {
        try {
            statement.setInt(1, studentCount);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return null;
    }

    private List<Group> parseGroups(ResultSet resultSet) {
        List<Group> groups = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt(GROUP_ID_COLUMN);
                String name = resultSet.getString(GROUP_NAME_COLUMN);
                Group group = new Group(id, name);
                groups.add(group);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return groups;
    }
}
