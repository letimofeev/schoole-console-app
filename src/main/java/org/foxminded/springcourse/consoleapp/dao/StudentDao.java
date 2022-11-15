package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDao {

    void save(Student student);

    List<Student> findAll();

    List<Student> findAllByCourseName(String courseName);

    Optional<Student> find(Student student);

    void update(Student student);

    void delete(Student student);

    void addStudentCourse(Student student, Course course);

    void deleteStudentCourse(Student student, Course course);
}
