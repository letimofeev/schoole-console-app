package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Student;

import java.util.List;

public interface StudentDao {

    void save(Student student);

    List<Student> findAll();

    List<Student> findAllByCourseName(String courseName);

    void deleteById(int id);

    void addStudentCourse(int studentId, int courseId);

    void deleteStudentCourse(int studentId, int courseId);
}
