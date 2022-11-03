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
    public String findAllByCourseName(@PatternGroup(1) String courseName) {
        List<Student> students = studentDao.findAllByCourseName(courseName);
        return studentFormatter.formatStudents(students);
    }

    @CommandMatching(regex = "Add student group id = (\\d+), first name = (\\w+), last name = (\\w+)")
    public String addStudent(@PatternGroup(1) int groupId,
                             @PatternGroup(2) String firstName,
                             @PatternGroup(3) String lastName) {
        Student student = new Student(groupId, firstName, lastName);
        studentDao.save(student);
        return studentFormatter.formatStudent(student);
    }

    @CommandMatching(regex = "Delete student with id = (\\d+)")
    public String deleteStudentById(@PatternGroup(1) int studentId) {
        studentDao.deleteById(studentId, Student.class);
        return "Student was deleted\n";
    }

    @CommandMatching(regex = "Add student with id = (\\d+) to course with id = (\\d+)")
    public String addStudentCourse(@PatternGroup(1) int studentId, @PatternGroup(2) int courseId) {
        studentDao.addStudentCourse(studentId, courseId);
        return "Student was added to course\n";
    }

    @CommandMatching(regex = "Remove student with id = (\\d+) from course with id = (\\d+)")
    public String deleteStudentCourse(@PatternGroup(1) int studentId, @PatternGroup(2) int courseId) {
        studentDao.deleteStudentCourse(studentId, courseId);
        return "Student was removed from course\n";
    }
}
