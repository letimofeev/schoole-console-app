package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.annotation.CommandMatching;
import org.foxminded.springcourse.consoleapp.annotation.PatternGroup;
import org.foxminded.springcourse.consoleapp.dao.StudentDao;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.foxminded.springcourse.consoleapp.view.StudentFormatter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentCommandHandler extends CommandHandler {

    private final StudentDao studentDao;
    private final StudentFormatter studentFormatter;

    public StudentCommandHandler(StudentDao studentDao, StudentFormatter studentFormatter) {
        this.studentDao = studentDao;
        this.studentFormatter = studentFormatter;
    }

    @CommandMatching(regex = "Find all students with course name = (\\w+)")
    public String findAllByCourseName(@PatternGroup String courseName) {
        List<Student> students = studentDao.findAllByCourseName(courseName);
        return studentFormatter.formatStudents(students);
    }

    @CommandMatching(regex = "Add student group id = (\\d+), first name = (\\w+), last name = (\\w+)")
    public String addStudent(@PatternGroup(1) int groupId,
                             @PatternGroup(2) String firstName,
                             @PatternGroup(3) String lastName) {
        Student student = new Student(0, groupId, firstName, lastName);
        studentDao.save(student);
        return studentFormatter.formatStudent(student);
    }

    @CommandMatching(regex = "Delete student with id = (\\d+)")
    public String deleteStudentById(@PatternGroup int studentId) {
        studentDao.deleteById(studentId, Student.class);
        return "Student was deleted\n";
    }
}
