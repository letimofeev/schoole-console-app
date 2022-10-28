package org.foxminded.springcourse.consoleapp.dao;

public interface CrudQueryBuilder<T> {

    String buildSaveQuery(T entity);
}
