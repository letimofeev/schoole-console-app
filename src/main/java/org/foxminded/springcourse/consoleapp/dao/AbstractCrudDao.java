package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.exception.DaoException;
import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.foxminded.springcourse.consoleapp.service.EntityDataMapper;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractCrudDao<T, ID> {

    protected final ConnectionConfig connectionConfig;
    protected final CrudQueryBuilder<T, ID> queryBuilder;
    protected final EntityDataMapper<T> dataMapper;

    public AbstractCrudDao(ConnectionConfig connectionConfig, CrudQueryBuilder<T, ID> queryBuilder,
                           EntityDataMapper<T> dataMapper) {
        this.connectionConfig = connectionConfig;
        this.queryBuilder = queryBuilder;
        this.dataMapper = dataMapper;
    }

    public void save(T entity) {
        try {
            String query = queryBuilder.buildSaveQuery(entity);
            genericExecuteQuery(query, statement -> {
                dataMapper.bindUpdatableColumns(statement, entity);
                return null;
            }, resultSet -> {
                dataMapper.getNextAndBindEntity(entity, resultSet);
                return null;
            });
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public Optional<T> findById(ID id, Class<T> entityClass) {
        try {
            String query = queryBuilder.buildFindByIdQuery(id, entityClass);
            T entity = entityClass.getConstructor().newInstance();
            genericExecuteQuery(query, statement -> {
                dataMapper.bindValue(statement, id);
                return null;
            }, resultSet -> {
                dataMapper.getNextAndBindEntity(entity, resultSet);
                return null;
            });
            if (isEntityEmpty(entity, entityClass)) {
                return Optional.empty();
            }
            return Optional.of(entity);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public void update(T entity) {
        try {
            String query = queryBuilder.buildUpdateQuery(entity);
            genericExecuteUpdateQuery(query, statement -> {
                int bindValuesNumber = dataMapper.bindUpdatableColumns(statement, entity);
                int lastBindIndex = bindValuesNumber + 1;
                dataMapper.bindIdValue(statement, entity, lastBindIndex);
                return null;
            });
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public void deleteById(ID id, Class<T> entityClass) {
        try {
            String query = queryBuilder.buildDeleteByIdQuery(id, entityClass);
            genericExecuteUpdateQuery(query, statement -> {
                dataMapper.bindValue(statement, id);
                return null;
            });
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

    protected void genericExecuteUpdateQuery(String query,
                                             Function<PreparedStatement, Void> prepareStatement) {
        try (Connection connection = createConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                prepareStatement.apply(statement);
                statement.executeUpdate();
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

    private boolean isEntityEmpty(T entity, Class<T> entityClass) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        T empty = entityClass.getConstructor().newInstance();
        return empty.equals(entity);
    }
}
