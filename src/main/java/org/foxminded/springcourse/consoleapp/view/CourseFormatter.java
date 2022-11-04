package org.foxminded.springcourse.consoleapp.view;

import org.foxminded.springcourse.consoleapp.model.Course;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseFormatter {

    public String formatCourses(List<Course> courses) {
        if (courses.isEmpty()) {
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
