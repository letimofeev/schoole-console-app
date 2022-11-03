package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.service.EntityDataMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CourseDao extends AbstractCrudDao<Course, Integer> {

    public CourseDao(ConnectionConfig connectionConfig,
                     CrudQueryBuilder<Course, Integer> queryBuilder,
                     EntityDataMapper<Course> dataMapper) {
        super(connectionConfig, queryBuilder, dataMapper);
    }
}
