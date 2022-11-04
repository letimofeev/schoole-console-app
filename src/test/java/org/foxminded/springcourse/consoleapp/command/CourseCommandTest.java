package org.foxminded.springcourse.consoleapp.command;

import org.foxminded.springcourse.consoleapp.dao.CourseDao;
import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.view.CourseFormatter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@TestInstance(PER_CLASS)
class CourseCommandTest {

    @Mock
    private CourseDao courseDao;

    @Mock
    private CourseFormatter courseFormatter;

    @InjectMocks
    private CourseCommand command;

    @BeforeAll
    void setUp() {
        openMocks(this);

        List<Course> courses = List.of(new Course(10, "Name", "Desc"));

        when(courseDao.findAll()).thenReturn(courses);

        when(courseFormatter.formatCourses(courses)).thenReturn("Formatted courses");
    }

    @Test
    void findAllCourses_shouldReturnFormattedCourses_whenInputIsEmpty() {
        String expected = "Formatted courses";
        String actual = command.findAllCourses();

        assertEquals(expected, actual);
    }
}