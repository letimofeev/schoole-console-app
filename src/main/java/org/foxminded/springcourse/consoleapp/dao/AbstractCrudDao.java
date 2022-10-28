package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.exception.DaoException;
import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;

import java.sql.*;
import java.util.function.Function;

public abstract class AbstractCrudDao<T, ID> {

    protected final ConnectionConfig connectionConfig;
    protected final CrudQueryBuilder<T, ID> queryBuilder;
    protected final EntityDataMapper<T, ID> dataMapper;

    public AbstractCrudDao(ConnectionConfig connectionConfig, CrudQueryBuilder<T, ID> queryBuilder,
                           EntityDataMapper<T, ID> dataMapper) {
        this.connectionConfig = connectionConfig;
        this.queryBuilder = queryBuilder;
        this.dataMapper = dataMapper;
    }

    public void save(T entity) {
        try {
            String query = queryBuilder.buildSaveQuery(entity);
            genericExecuteQuery(query, statement -> {
                dataMapper.bindAllColumns(statement, entity);
                return null;
            }, resultSet -> {
                dataMapper.takeNextAndBindEntity(entity, resultSet);
                return null;
            });
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public T findById(ID id, Class<T> entityClass) {
        try {
            String query = queryBuilder.buildFindByIdQuery(id, entityClass);
            T entity = entityClass.getConstructor().newInstance();
            genericExecuteQuery(query, statement -> {
                dataMapper.bindIdColumn(statement, id);
                return null;
            }, resultSet -> {
                dataMapper.takeNextAndBindEntity(entity, resultSet);
                return null;
            });
            return entity;
        } catch (Exception e) {
            throw new DaoException(e);
        }
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
