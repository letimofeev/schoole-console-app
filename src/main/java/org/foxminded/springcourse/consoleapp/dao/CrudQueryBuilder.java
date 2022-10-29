package org.foxminded.springcourse.consoleapp.dao;

public interface CrudQueryBuilder<T, ID> {

    String buildSaveQuery(T entity);

    String buildFindByIdQuery(ID id, Class<T> entityClass);

    String buildFindAllQuery(Class<T> entityClass);

    String buildUpdateQuery(T entity);

    String buildDeleteByIdQuery(ID id, Class<T> entityClass);
}
