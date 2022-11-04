package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Course;

import java.util.List;

public interface CourseDao {

     List<Course> findAll();

}
