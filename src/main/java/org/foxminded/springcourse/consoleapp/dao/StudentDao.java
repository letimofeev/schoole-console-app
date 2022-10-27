package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.exception.DaoException;
import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class StudentDao {

    private final ConnectionConfig connectionConfig;

    public StudentDao(ConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    public void save(Student student) {
        try (Connection connection = createConnection()) {
            String sql = "INSERT INTO students (group_id, first_name, last_name) " +
                    "VALUES (?, ?, ?) " +
                    "RETURNING student_id;";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, student.getGroupId());
                statement.setString(2, student.getFirstName());
                statement.setString(3, student.getLastName());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    student.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Connection createConnection() throws SQLException {
        String url = connectionConfig.getUrl();
        String user = connectionConfig.getUser();
        String password = connectionConfig.getPassword();
        return DriverManager.getConnection(url, user, password);
    }
}
