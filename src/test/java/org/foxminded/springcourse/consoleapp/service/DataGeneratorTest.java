package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class DataGeneratorTest {

    private final DataGenerator dataGenerator = new DataGenerator();

    @Test
    void generateRandomGroups_shouldReturnGroups_whenInputIsGroupsCount() {
        int count = 100;

        List<Group> groups = dataGenerator.generateRandomGroups(count);

        assertEquals(count, groups.size());
    }

    @Test
    void generateCourses_shouldReturnCourses_whenTrue() {
        List<Course> courses = dataGenerator.generateCourses();
        assertFalse(courses.isEmpty());
    }

    @Test
    void generateRandomStudents_shouldReturnStudents_whenInputIsStudentsCountAndGroups() {
        int count = 10;

        List<Group> groups = List.of(new Group(1, "one"), new Group(2, "two"), new Group(3,"three"));
        List<Integer> groupsIds = List.of(1, 2, 3);

        List<Student> students = dataGenerator.generateRandomStudents(count, groups);

        assertEquals(count, students.size());

        for (Student student : students) {
            int groupId = student.getGroupId();
            assertTrue(groupsIds.contains(groupId));
        }
    }

    @Test
    void getRandomElement_shouldReturnContainingElement_whenInputIsCollection() {
        List<Integer> elements = IntStream.rangeClosed(1, 1000).boxed().collect(Collectors.toList());

        for (int i = 0; i < 100; i++) {
            int randomElement = dataGenerator.getRandomElement(elements);
            assertTrue(elements.contains(randomElement));
        }
    }
}