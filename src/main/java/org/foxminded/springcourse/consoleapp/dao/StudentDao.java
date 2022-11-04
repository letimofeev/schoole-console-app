package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDao {

    void save(Student student);

    List<Student> findAll();

    List<Student> findAllByCourseName(String courseName);

    Optional<Student> findById(int id);

    void update(Student student);

    void deleteById(int id);

    void addStudentCourse(int studentId, int courseId);

    void deleteStudentCourse(int studentId, int courseId);
}
