package org.foxminded.springcourse.consoleapp.view;

import org.foxminded.springcourse.consoleapp.model.Student;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentFormatter {

    public String formatStudents(List<Student> students) {
        if (students.isEmpty()) {
            return "No students found\n";
        }
        StringBuilder formattedStudents = new StringBuilder();
        for (Student student : students) {
            String formattedStudent = formatStudent(student);
            formattedStudents.append(formattedStudent);
            formattedStudents.append("\n");
        }
        return formattedStudents.toString();
    }

    public String formatStudent(Student student) {
        return String.format("Student: id = %d, group id = %d, first name = %s, last name = %s\n",
                student.getId(),
                student.getGroupId(),
                student.getFirstName(),
                student.getLastName());
    }
}
