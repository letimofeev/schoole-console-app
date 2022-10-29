package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.foxminded.springcourse.consoleapp.service.EntityDataMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDao extends AbstractCrudDao<Student, Integer> {

    public StudentDao(ConnectionConfig connectionConfig,
                      CrudQueryBuilder<Student, Integer> queryBuilder,
                      EntityDataMapper<Student> dataBinder) {
        super(connectionConfig, queryBuilder, dataBinder);
    }

    public <ID2> void addStudentCourse(int studentId, ID2 courseId) {
        String sql = "INSERT INTO students_courses VALUES (?, ?)";
        genericExecuteUpdateQuery(sql, statement -> dataMapper.bindValues(statement, studentId, courseId));
    }
}
