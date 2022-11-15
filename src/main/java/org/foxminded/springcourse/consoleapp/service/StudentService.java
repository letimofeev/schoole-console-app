package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.model.Student;

import java.util.List;

public interface StudentService {

    void save(Student student);

    void delete(Student student);

    List<Student> findAll();

    List<Student> findAllByCourseName(String courseName);

    void addStudentCourse(Student student, Course course);

    void deleteStudentCourse(Student student, Course course);
}
