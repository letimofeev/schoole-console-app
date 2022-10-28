package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.exception.DaoException;
import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class StudentDao extends AbstractDao<Student, Integer> {

    public StudentDao(ConnectionConfig connectionConfig) {
        super(connectionConfig);
    }

    public void save(Student student) {
        String sql = "INSERT INTO students (group_id, first_name, last_name) " +
                "VALUES (?, ?, ?) " +
                "RETURNING student_id;";
        genericExecuteQuery(sql, statement -> bindStudent(statement, student),
                resultSet -> parseStudentId(resultSet, student));

    }

    private Void bindStudent(PreparedStatement statement, Student student) {
        try {
            statement.setInt(1, student.getGroupId());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return null;
    }

    private Void parseStudentId(ResultSet resultSet, Student student) {
        try {
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                student.setId(id);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return null;
    }
}
