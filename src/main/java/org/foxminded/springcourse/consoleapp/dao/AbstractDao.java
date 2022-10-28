package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.exception.DaoException;
import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;

import java.sql.*;
import java.util.function.Function;

public abstract class AbstractDao<T, ID> {

    protected final ConnectionConfig connectionConfig;

    protected AbstractDao(ConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    protected <R> R genericExecuteQuery(String query,
                                        Function<PreparedStatement, Void> prepareStatement,
                                        Function<ResultSet, R> parseResultSet) {
        try (Connection connection = createConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                prepareStatement.apply(statement);
                ResultSet resultSet = statement.executeQuery();
                return parseResultSet.apply(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    protected Connection createConnection() throws SQLException {
        String url = connectionConfig.getUrl();
        String user = connectionConfig.getUser();
        String password = connectionConfig.getPassword();
        return DriverManager.getConnection(url, user, password);
    }
}
