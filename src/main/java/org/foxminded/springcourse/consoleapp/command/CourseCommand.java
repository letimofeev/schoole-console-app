package org.foxminded.springcourse.consoleapp.command;

import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.service.CourseService;
import org.foxminded.springcourse.consoleapp.view.CourseFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@ShellComponent
public class CourseCommand {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CourseService courseService;
    private final CourseFormatter courseFormatter;

    public CourseCommand(CourseService courseService, CourseFormatter courseFormatter) {
        this.courseService = courseService;
        this.courseFormatter = courseFormatter;
    }

    @ShellMethod("Find all courses")
    public String findAllCourses() {
        try {
            List<Course> courses = courseService.findAll();
            return courseFormatter.formatCourses(courses);
        } catch (Exception e) {
            log.error("Exception during finding all courses, nested exception: {}", e.toString());
            throw e;
        }
    }
}
