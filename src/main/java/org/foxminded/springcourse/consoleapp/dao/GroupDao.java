package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.exception.DaoException;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GroupDao {

    private final ConnectionConfig connectionConfig;

    private static final String GROUP_ID_COLUMN = "group_id";
    private static final String GROUP_NAME_COLUMN = "group_name";

    public GroupDao(ConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    public List<Group> findAllWithStudentCountLessThanEqual(int studentCount) {
        List<Group> groups = new ArrayList<>();
        try (Connection connection = createConnection()) {
            String sql = "SELECT groups.group_id, group_name\n" +
                    "FROM groups\n" +
                    "JOIN students s on groups.group_id = s.group_id\n" +
                    "GROUP BY groups.group_id, group_name\n" +
                    "HAVING count(*) > ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, studentCount);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt(GROUP_ID_COLUMN);
                    String name = resultSet.getString(GROUP_NAME_COLUMN);
                    Group group = new Group(id, name);
                    groups.add(group);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return groups;
    }

    private Connection createConnection() throws SQLException {
        String url = connectionConfig.getUrl();
        String user = connectionConfig.getUser();
        String password = connectionConfig.getPassword();
        return DriverManager.getConnection(url, user, password);
    }
}
