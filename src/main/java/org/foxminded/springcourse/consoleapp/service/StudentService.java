package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.model.Student;

import java.util.List;

public interface StudentService {

    void save(Student student);

    void deleteById(int id);

    List<Student> findAll();

    List<Student> findAllByCourseName(String courseName);

    void addStudentCourse(int studentId, int courseId);

    void deleteStudentCourse(int studentId, int courseId);
}
