package org.foxminded.springcourse.consoleapp.view;

import org.foxminded.springcourse.consoleapp.model.Course;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseFormatterTest {

    private final CourseFormatter courseFormatter = new CourseFormatter();

    @Test
    void formatCourses_shouldReturnNotification_whenInputIsEmptyList() {
        List<Course> courses = Collections.emptyList();

        String expected = "No courses found";
        String actual = courseFormatter.formatCourses(courses);

        assertEquals(expected, actual);
    }

    @Test
    void formatGroups_shouldReturnFormattedCourses_whenInputIsCourseList() {
        List<Course> courses = List.of(new Course(123, "Amogus", "Desc"),
                new Course(322, "Kurs", "123455"),
                new Course(555, "Kursk", "88005553555"),
                new Course(124, "AnotherOne", "Korn"),
                new Course(122, "Led", "Zeppelin"));

        String expected = "Course: id = 123, course name = Amogus, course description = Desc\n" +
                "Course: id = 322, course name = Kurs, course description = 123455\n" +
                "Course: id = 555, course name = Kursk, course description = 88005553555\n" +
                "Course: id = 124, course name = AnotherOne, course description = Korn\n" +
                "Course: id = 122, course name = Led, course description = Zeppelin";
        String actual = courseFormatter.formatCourses(courses);

        assertEquals(expected, actual);
    }

    @Test
    void formatGroup_shouldReturnFormattedGroup_whenInputIsGroup() {
        Course course = new Course(1, "Queen", "____");

        String expected = "Course: id = 1, course name = Queen, course description = ____";
        String actual = courseFormatter.formatCourse(course);

        assertEquals(expected, actual);
    }
}
