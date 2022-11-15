package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDao {

     void save(Course course);

     List<Course> findAll();

     Optional<Course> findById(int id);

     void update(Course course);

     void delete(Course course);
}
