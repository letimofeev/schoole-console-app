package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.dao.CourseDao;
import org.foxminded.springcourse.consoleapp.dao.GroupDao;
import org.foxminded.springcourse.consoleapp.dao.StudentDao;
import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@TestInstance(PER_CLASS)
class DatabasePreparerTest {

    @Mock
    private SqlScriptRunner scriptRunner;

    @Mock
    private DataGenerator dataGenerator;

    @Mock
    private StudentDao studentDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private CourseDao courseDao;

    @InjectMocks
    private DatabasePreparer databasePreparer;

    @BeforeAll
    void setUp() {
        openMocks(this);

        List<Course> courses = List.of(new Course("course1", "desc1"), new Course("course2", "desc2"));
        List<Group> groups = List.of(new Group("one"), new Group("two"), new Group("three"));
        List<Student> students = List.of(new Student(0, "Name1", "1"), new Student(0, "Name2", "2"));

        when(dataGenerator.generateCourses()).thenReturn(courses);
        when(dataGenerator.generateRandomGroups(10)).thenReturn(groups);
        when(dataGenerator.generateRandomStudents(200, groups)).thenReturn(students);
        when(dataGenerator.getRandomElement(any())).thenCallRealMethod();
    }

    @Test
    void prepareBase_shouldRunInitialScriptAndCallDao_whenInputIsInitialScriptPath() {
        String scriptPath = "path";

        databasePreparer.prepareBase(scriptPath);

        verify(scriptRunner, times(1)).executeSqlScript(scriptPath);
        verify(dataGenerator, times(1)).generateRandomStudents(anyInt(), any());
        verify(dataGenerator, times(1)).generateRandomGroups(anyInt());
        verify(dataGenerator, times(1)).generateCourses();
        verify(studentDao, times(2)).save(any(Student.class));
        verify(groupDao, times(3)).save(any(Group.class));
        verify(courseDao, times(2)).save(any(Course.class));
    }
}