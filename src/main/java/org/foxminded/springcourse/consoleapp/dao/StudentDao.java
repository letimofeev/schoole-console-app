package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.foxminded.springcourse.consoleapp.service.EntityDataMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentDao extends AbstractCrudDao<Student, Integer> {

    public StudentDao(ConnectionConfig connectionConfig,
                      CrudQueryBuilder<Student, Integer> queryBuilder,
                      EntityDataMapper<Student> dataMapper) {
        super(connectionConfig, queryBuilder, dataMapper);
    }

    public void addStudentCourse(int studentId, Object courseId) {
        String sql = "INSERT INTO students_courses VALUES (?, ?)";
        genericExecuteUpdateQuery(sql, statement -> dataMapper.bindValues(statement, studentId, courseId));
    }

    public List<Student> findAllByCourseName(String courseName) {
        String sql = "SELECT students.student_id, group_id, first_name, last_name\n" +
                "FROM students\n" +
                "JOIN students_courses sc on students.student_id = sc.student_id\n" +
                "JOIN courses c on c.course_id = sc.course_id\n" +
                "WHERE c.course_name = ?;";
        return genericExecuteQuery(sql, statement -> dataMapper.bindValues(statement, courseName),
                resultSet -> dataMapper.collectEntities(Student.class, resultSet));
    }
}
