package org.foxminded.springcourse.consoleapp.dao;

public interface CrudQueryBuilder<T, ID> {

    String buildSaveQuery(T entity);

    String buildFindByIdQuery(ID id, Class<T> entityClass);
}
