package org.foxminded.springcourse.consoleapp.command;

import org.foxminded.springcourse.consoleapp.model.Student;
import org.foxminded.springcourse.consoleapp.service.StudentService;
import org.foxminded.springcourse.consoleapp.view.StudentFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
public class StudentCommand {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final StudentService studentService;
    private final StudentFormatter studentFormatter;

    public StudentCommand(StudentService studentService, StudentFormatter studentFormatter) {
        this.studentService = studentService;
        this.studentFormatter = studentFormatter;
    }

    @ShellMethod("Find all students")
    public String findAllStudents() {
        log.debug("Entering find-all-students command");
        List<Student> students = studentService.findAll();
        return studentFormatter.formatStudents(students);
    }

    @ShellMethod("Find all students by course name")
    public String findAllStudentsByCourseName(@ShellOption("--name") String courseName) {
        log.debug("Entering find-all-students-by-course-name command with parameters: --name = {}", courseName);
        List<Student> students = studentService.findAllByCourseName(courseName);
        return studentFormatter.formatStudents(students);
    }

    @ShellMethod("Add new student")
    public String addStudent(@ShellOption("--group-id") int groupId,
                             @ShellOption("--first-name") String firstName,
                             @ShellOption("--last-name") String lastName) {
        log.debug("Entering add-student command with parameters: --group-id = {}, --first-name = {} --last-name = {}",
                groupId, firstName, lastName);
        Student student = new Student(groupId, firstName, lastName);
        studentService.save(student);
        log.info("Saved student: id = {}, groupId = {}, firstName = {}, lastName = {}",
                student.getStudentId(), student.getGroupId(), student.getFirstName(), student.getLastName());
        return studentFormatter.formatStudent(student);
    }

    @ShellMethod("Delete student by id")
    public String deleteStudentById(@ShellOption("--id") int id) {
        log.debug("Entering delete-student-by-id command with parameters: --id = {}", id);
        studentService.deleteById(id);
        log.info("Student with id = {} deleted", id);
        return "Student deleted";
    }

    @ShellMethod("Add student to a course")
    public String addStudentCourse(@ShellOption("--student-id") int studentId,
                                   @ShellOption("--course-id") int courseId) {
        log.debug("Entering add-student-course command with parameters: --student-id = {}, --course-id = {}",
                studentId, courseId);
        studentService.addStudentCourse(studentId, courseId);
        log.info("Student with id = {} added to the course with id = {}", studentId, courseId);
        return "Student added to the course";
    }

    @ShellMethod("Delete student from a course")
    public String deleteStudentCourse(@ShellOption("--student-id") int studentId,
                                      @ShellOption("--course-id") int courseId) {
        log.debug("Entering delete-student-course command with parameters: --student-id = {}, --course-id = {}",
                studentId, courseId);
        studentService.deleteStudentCourse(studentId, courseId);
        log.info("Student with id = {} deleted from the course with id = {}", studentId, courseId);
        return "Student deleted from the course";
    }
}
