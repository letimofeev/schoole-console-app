package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.dao.CourseDao;
import org.foxminded.springcourse.consoleapp.model.Course;
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
class CourseServiceImplTest {

    @Mock
    private CourseDao courseDao;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeAll
    void setUp() {
        openMocks(this);

        List<Course> courses = List.of(new Course(10, "Name", "Desc"));

        when(courseDao.findAll()).thenReturn(courses);
    }

    @Test
    void findAll_shouldReturnCoursesFromDao_whenInputIsEmpty() {
        List<Course> expected = List.of(new Course(10, "Name", "Desc"));
        List<Course> actual = courseService.findAll();

        assertEquals(expected, actual);
    }
}
