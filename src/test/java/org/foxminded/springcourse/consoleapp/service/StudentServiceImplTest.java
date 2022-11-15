package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.dao.StudentDao;
import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@TestInstance(PER_CLASS)
class StudentServiceImplTest {

    @Mock
    private StudentDao studentDao;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeAll
    void setUp() {
        openMocks(this);

        List<Student> studentsByCourse = List.of(new Student(1, 2, "One", "One1"),
                new Student(2, 3, "Two", "Two2"),
                new Student(3, 4, "Three", "Three3"));

        List<Student> allStudents = List.of(new Student(112, 222, "Oleg", "Nikitov"),
                new Student(100, 222, "Nikita", "Olegov"));

        when(studentDao.findAllByCourseName("course")).thenReturn(studentsByCourse);
        when(studentDao.findAll()).thenReturn(allStudents);

        doAnswer(invocation -> {
            Student student = invocation.getArgument(0);
            student.setStudentId(1);
            return null;
        }).when(studentDao).save(any());
    }

    @AfterEach
    void tearDown() {
        clearInvocations(studentDao);
    }

    @Test
    void findAll_shouldReturnAllStudentsFromDao_whenCallMethod() {
        List<Student> expected = List.of(new Student(112, 222, "Oleg", "Nikitov"),
                new Student(100, 222, "Nikita", "Olegov"));
        List<Student> actual = studentService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void findAllByCourseName_shouldReturnStudentsByCourseName_whenInputIsCourseName() {
        String courseName = "course";

        List<Student> expected = List.of(new Student(1, 2, "One", "One1"),
                new Student(2, 3, "Two", "Two2"),
                new Student(3, 4, "Three", "Three3"));
        List<Student> actual = studentService.findAllByCourseName(courseName);

        assertEquals(expected, actual);
    }

    @Test
    void save_shouldInjectStudentId_whenInputIsArgs() {
        Student actual = new Student(11, "Name", "LastName");

        Student expected = new Student(1, 11, "Name", "LastName");
        studentService.save(actual);

        assertEquals(expected, actual);
    }

    @Test
    void deleteStudentById_shouldInvokeDaoDeleteByIdMethod_whenInputIsId() {
        Student student = new Student();
        student.setStudentId(1000);

        studentService.delete(student);

        verify(studentDao, times(1)).delete(student);
    }

    @Test
    void addStudentCourse_shouldInvokeDaoMethod_whenInputIsIds() {
        Student student = new Student();
        Course course = new Course();
        student.setStudentId(1000);
        course.setCourseId(1001);

        studentService.addStudentCourse(student, course);

        verify(studentDao, times(1)).addStudentCourse(student, course);
    }

    @Test
    void deleteStudentCourse_shouldInvokeDaoMethod_whenInputIsIds() {
        Student student = new Student();
        Course course = new Course();
        student.setStudentId(1);
        course.setCourseId(2);

        studentService.deleteStudentCourse(student, course);

        verify(studentDao, times(1)).deleteStudentCourse(student, course);
    }
}
