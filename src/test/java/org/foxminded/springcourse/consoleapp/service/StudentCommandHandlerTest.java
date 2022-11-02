package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.dao.StudentDao;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.foxminded.springcourse.consoleapp.view.StudentFormatter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@TestInstance(PER_CLASS)
class StudentCommandHandlerTest {

    @Mock
    private StudentDao studentDao;

    @Mock
    private StudentFormatter studentFormatter;

    @InjectMocks
    private StudentCommandHandler commandHandler;

    @BeforeAll
    void setUp() {
        openMocks(this);

        List<Student> studentsByCourse = List.of();
        when(studentDao.findAllByCourseName("course")).thenReturn(studentsByCourse);
        when(studentFormatter.formatStudents(studentsByCourse)).thenReturn("Formatted studentsByCourse by course name");

        doAnswer(invocation -> {
            Student student = invocation.getArgument(0);
            student.setId(1);
            return null;
        }).when(studentDao).save(any());
        when(studentFormatter.formatStudent(new Student(1, 10, "Name", "LastName"))).thenReturn("Student(1)");
    }

    @Test
    void findAllByCourseName_shouldReturnFormattedAllByCourseName_whenInputIsCourseName() {
        String courseName = "course";

        String expected = "Formatted studentsByCourse by course name";
        String actual = commandHandler.findAllByCourseName(courseName);

        assertEquals(expected, actual);
    }

    @Test
    void addStudent_shouldReturnStudentAdditionMessage_whenInputIsArgs() {
        String expected = "Student(1)";
        String actual = commandHandler.addStudent(10, "Name", "LastName");

        assertEquals(expected, actual);
    }

    @Test
    void deleteStudentById_shouldReturnStudentDeletionMessage_whenInputIsId() {
        String expected = "Student was deleted\n";
        String actual = commandHandler.deleteStudentById(1000);

        assertEquals(expected, actual);
    }

    @Test
    void addStudentCourse_shouldReturnStudentCourseAdditionMessage_whenInputIsIds() {
        String expected = "Student was added to course\n";
        String actual = commandHandler.addStudentCourse(1000, 1000);

        assertEquals(expected, actual);
    }

    @Test
    void deleteStudentCourse_shouldReturnStudentCourseDeletionMessage_whenInputIsIds() {
        String expected = "Student was removed from course\n";
        String actual = commandHandler.deleteStudentCourse(1000, 1000);

        assertEquals(expected, actual);
    }
}