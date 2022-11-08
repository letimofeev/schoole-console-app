package org.foxminded.springcourse.consoleapp.command;

import org.foxminded.springcourse.consoleapp.model.Student;
import org.foxminded.springcourse.consoleapp.service.StudentService;
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
class StudentCommandTest {

    @Mock
    private StudentService studentService;

    @Mock
    private StudentFormatter studentFormatter;

    @InjectMocks
    private StudentCommand command;

    @BeforeAll
    void setUp() {
        openMocks(this);

        List<Student> studentsByCourse = List.of(new Student(1, 2, "One", "One1"),
                new Student(2, 3, "Two", "Two2"),
                new Student(3, 4, "Three", "Three3"));

        List<Student> allStudents = List.of(new Student(112, 222, "Oleg", "Nikitov"),
                new Student(100, 222, "Nikita", "Olegov"));

        when(studentService.findAllByCourseName("course")).thenReturn(studentsByCourse);
        when(studentService.findAll()).thenReturn(allStudents);

        when(studentFormatter.formatStudents(studentsByCourse)).thenReturn("Formatted studentsByCourse by course name");
        when(studentFormatter.formatStudents(allStudents)).thenReturn("Formatted all students");

        doAnswer(invocation -> {
            Student student = invocation.getArgument(0);
            student.setStudentId(1);
            return null;
        }).when(studentService).save(any());

        when(studentFormatter.formatStudent(new Student(1, 10, "Name", "LastName"))).thenReturn("Student(1)");
    }

    @Test
    void findAllStudents_shouldReturnFormattedStudents_whenCallMethod() {
        String expected = "Formatted all students";
        String actual = command.findAllStudents();

        assertEquals(expected, actual);
    }

    @Test
    void findAllStudentsByCourseName_shouldReturnFormattedAllByCourseName_whenInputIsCourseName() {
        String courseName = "course";

        String expected = "Formatted studentsByCourse by course name";
        String actual = command.findAllStudentsByCourseName(courseName);

        assertEquals(expected, actual);
    }

    @Test
    void addStudent_shouldReturnStudentAdditionMessage_whenInputIsArgs() {
        String expected = "Student(1)";
        String actual = command.addStudent(10, "Name", "LastName");

        assertEquals(expected, actual);
    }

    @Test
    void deleteStudentById_shouldReturnStudentDeletionMessage_whenInputIsId() {
        String expected = "Student deleted";
        String actual = command.deleteStudentById(1000);

        assertEquals(expected, actual);
    }

    @Test
    void addStudentCourse_shouldReturnStudentCourseAdditionMessage_whenInputIsIds() {
        String expected = "Student added to the course";
        String actual = command.addStudentCourse(1000, 1000);

        assertEquals(expected, actual);
    }

    @Test
    void deleteStudentCourse_shouldReturnStudentCourseDeletionMessage_whenInputIsIds() {
        String expected = "Student deleted from the course";
        String actual = command.deleteStudentCourse(1000, 1000);

        assertEquals(expected, actual);
    }
}
