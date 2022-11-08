package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.model.Course;

import java.util.List;

public interface CourseService {

    List<Course> findAll();
}
