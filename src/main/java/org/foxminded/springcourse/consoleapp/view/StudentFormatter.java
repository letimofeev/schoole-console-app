package org.foxminded.springcourse.consoleapp.view;

import org.foxminded.springcourse.consoleapp.model.Student;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentFormatter {

    public String formatStudents(List<Student> students) {
        if (students.isEmpty()) {
            return "No students found";
        }
        return students.stream()
                .map(this::formatStudent)
                .collect(Collectors.joining("\n"));
    }

    public String formatStudent(Student student) {
        return String.format("Student: id = %d, group id = %d, first name = %s, last name = %s",
                student.getStudentId(),
                student.getGroupId(),
                student.getFirstName(),
                student.getLastName());
    }
}
