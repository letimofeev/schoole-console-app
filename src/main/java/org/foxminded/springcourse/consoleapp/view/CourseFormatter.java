package org.foxminded.springcourse.consoleapp.view;

import org.foxminded.springcourse.consoleapp.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseFormatter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public String formatCourses(List<Course> courses) {
        if (courses.isEmpty()) {
            log.info("Empty courses list passed to formatter");
            return "No courses found";
        }
        return courses.stream()
                .map(this::formatCourse)
                .collect(Collectors.joining("\n"));
    }

    public String formatCourse(Course course) {
        return String.format("Course: id = %d, course name = %s, course description = %s",
                course.getCourseId(),
                course.getCourseName(),
                course.getCourseDescription());
    }
}
