package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.exception.DaoException;
import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;

import java.sql.*;
import java.util.function.Function;

public abstract class AbstractCrudDao<T, ID> {

    protected final ConnectionConfig connectionConfig;
    protected final CrudQueryBuilder<T> queryBuilder;
    protected final EntityDataMapper<T> dataMapper;

    public AbstractCrudDao(ConnectionConfig connectionConfig, CrudQueryBuilder<T> queryBuilder,
                           EntityDataMapper<T> dataMapper) {
        this.connectionConfig = connectionConfig;
        this.queryBuilder = queryBuilder;
        this.dataMapper = dataMapper;
    }

    public void save(T entity) {
        String query = queryBuilder.buildSaveQuery(entity);
        genericExecuteQuery(query, statement -> {
            dataMapper.bindStatement(statement, entity);
            return null;
        }, resultSet -> {
            try {
                if (resultSet.next()) {
                    dataMapper.bindEntity(entity, resultSet);
                }
            } catch (Exception e) {
                throw new DaoException(e);
            }
            return null;
        });
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
