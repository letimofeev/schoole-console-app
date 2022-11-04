package org.foxminded.springcourse.consoleapp.command;

import org.foxminded.springcourse.consoleapp.dao.CourseDao;
import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.view.CourseFormatter;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@ShellComponent
public class CourseCommand {

    private final CourseDao courseDao;
    private final CourseFormatter courseFormatter;

    public CourseCommand(CourseDao courseDao, CourseFormatter courseFormatter) {
        this.courseDao = courseDao;
        this.courseFormatter = courseFormatter;
    }

    @ShellMethod("Find all courses")
    public String findAllCourses() {
        List<Course> courses = courseDao.findAll();
        return courseFormatter.formatCourses(courses);
    }
}
