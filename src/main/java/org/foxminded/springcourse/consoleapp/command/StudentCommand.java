package org.foxminded.springcourse.consoleapp.command;

import org.foxminded.springcourse.consoleapp.dao.StudentDao;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.foxminded.springcourse.consoleapp.view.StudentFormatter;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
public class StudentCommand {

    private final StudentDao studentDao;
    private final StudentFormatter studentFormatter;

    public StudentCommand(StudentDao studentDao, StudentFormatter studentFormatter) {
        this.studentDao = studentDao;
        this.studentFormatter = studentFormatter;
    }

    @ShellMethod("Find all students")
    public String findAllStudents() {
        List<Student> students = studentDao.findAll();
        return studentFormatter.formatStudents(students);
    }

    @ShellMethod("Find all students by course name")
    public String findAllStudentsByCourseName(@ShellOption("--name") String courseName) {
        List<Student> students = studentDao.findAllByCourseName(courseName);
        return studentFormatter.formatStudents(students);
    }

    @ShellMethod("Add new student")
    public String addStudent(@ShellOption("--group-id") int groupId,
                             @ShellOption("--first-name") String firstName,
                             @ShellOption("--last-name") String lastName) {
        Student student = new Student(groupId, firstName, lastName);
        studentDao.save(student);
        return studentFormatter.formatStudent(student);
    }

    @ShellMethod("Delete student by id")
    public String deleteStudentById(@ShellOption("--id") int id) {
        studentDao.deleteById(id);
        return "Student deleted";
    }

    @ShellMethod("Add student to a course")
    public String addStudentCourse(@ShellOption("--student-id") int studentId,
                                   @ShellOption("--course-id") int courseId) {
        studentDao.addStudentCourse(studentId, courseId);
        return "Student added to the course";
    }

    @ShellMethod("Delete student from a course")
    public String deleteStudentCourse(@ShellOption("--student-id") int studentId,
                                      @ShellOption("--course-id") int courseId) {
        studentDao.deleteStudentCourse(studentId, courseId);
        return "Student deleted from the course";
    }
}
