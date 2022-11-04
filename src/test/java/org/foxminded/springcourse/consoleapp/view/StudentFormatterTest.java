package org.foxminded.springcourse.consoleapp.view;

import org.foxminded.springcourse.consoleapp.model.Student;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentFormatterTest {

    private final StudentFormatter studentFormatter = new StudentFormatter();

    @Test
    void formatStudents_shouldReturnNotification_whenInputIsEmptyList() {
        List<Student> students = Collections.emptyList();

        String expected = "No students found";
        String actual = studentFormatter.formatStudents(students);

        assertEquals(expected, actual);
    }

    @Test
    void formatStudents_shouldReturnFormattedStudents_whenInputIsStudentList() {
        List<Student> students = List.of(new Student(1, 1, "One", "Onenov"),
                new Student(2, 222, "Two", "Too Too"),
                new Student(3, 333, "Three", "Free"),
                new Student(4, 999, "Four", "Sour"));

        String expected = "Student: id = 1, group id = 1, first name = One, last name = Onenov\n" +
                "Student: id = 2, group id = 222, first name = Two, last name = Too Too\n" +
                "Student: id = 3, group id = 333, first name = Three, last name = Free\n" +
                "Student: id = 4, group id = 999, first name = Four, last name = Sour";
        String actual = studentFormatter.formatStudents(students);

        assertEquals(expected, actual);
    }

    @Test
    void formatStudent_shouldReturnFormattedStudent_whenInputIsStudent() {
        Student student = new Student(100000, 99, "Mihail", "Terent'ev");

        String expected = "Student: id = 100000, group id = 99, first name = Mihail, last name = Terent'ev";
        String actual = studentFormatter.formatStudent(student);

        assertEquals(expected, actual);
    }
}
