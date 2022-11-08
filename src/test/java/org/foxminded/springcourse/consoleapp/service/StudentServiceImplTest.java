package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.dao.StudentDao;
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
        studentService.deleteById(1000);

        verify(studentDao, times(1)).deleteById(1000);
    }

    @Test
    void addStudentCourse_shouldInvokeDaoMethod_whenInputIsIds() {
        studentService.addStudentCourse(1000, 1001);

        verify(studentDao, times(1)).addStudentCourse(1000, 1001);
    }

    @Test
    void deleteStudentCourse_shouldInvokeDaoMethod_whenInputIsIds() {
        studentService.deleteStudentCourse(1, 2);

        verify(studentDao, times(1)).deleteStudentCourse(1, 2);
    }
}
