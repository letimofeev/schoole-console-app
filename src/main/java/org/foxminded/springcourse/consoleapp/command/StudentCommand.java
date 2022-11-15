package org.foxminded.springcourse.consoleapp.command;

import org.foxminded.springcourse.consoleapp.model.Course;
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
        try {
            List<Student> students = studentService.findAll();
            return studentFormatter.formatStudents(students);
        } catch (Exception e) {
            log.error("Exception during finding all students, nested exception: {}", e.toString());
            throw e;
        }
    }

    @ShellMethod("Find all students by course name")
    public String findAllStudentsByCourseName(@ShellOption("--name") String courseName) {
        try {
            List<Student> students = studentService.findAllByCourseName(courseName);
            return studentFormatter.formatStudents(students);
        } catch (Exception e) {
            log.error("Exception during finding all students by course name, " +
                    "nested exception: {}", e.toString());
            throw e;
        }
    }

    @ShellMethod("Add new student")
    public String addStudent(@ShellOption("--group-id") int groupId,
                             @ShellOption("--first-name") String firstName,
                             @ShellOption("--last-name") String lastName) {
        try {
            Student student = new Student(groupId, firstName, lastName);
            studentService.save(student);
            log.info("Saved student: id = {}, groupId = {}, firstName = {}, lastName = {}",
                    student.getStudentId(), student.getGroupId(), student.getFirstName(), student.getLastName());
            return studentFormatter.formatStudent(student);
        } catch (Exception e) {
            log.error("Exception during adding student, nested exception: {}", e.toString());
            throw e;
        }
    }

    @ShellMethod("Delete student by id")
    public String deleteStudentById(@ShellOption("--id") int id) {
        try {
            Student student = new Student();
            student.setStudentId(id);
            studentService.delete(student);
            log.info("Student with id = {} deleted", id);
            return "Student deleted";
        } catch (Exception e) {
            log.error("Exception during deleting student, nested exception: {}", e.toString());
            throw e;
        }
    }

    @ShellMethod("Add student to a course")
    public String addStudentCourse(@ShellOption("--student-id") int studentId,
                                   @ShellOption("--course-id") int courseId) {
        try {
            Student student = new Student();
            Course course = new Course();
            student.setStudentId(studentId);
            course.setCourseId(courseId);
            studentService.addStudentCourse(student, course);
            log.info("Student with id = {} added to the course with id = {}", studentId, courseId);
            return "Student added to the course";
        } catch (Exception e) {
            log.error("Exception during adding student to the course, " +
                    "nested exception: {}", e.toString());
            throw e;
        }
    }

    @ShellMethod("Delete student from a course")
    public String deleteStudentCourse(@ShellOption("--student-id") int studentId,
                                      @ShellOption("--course-id") int courseId) {
        try {
            Student student = new Student();
            Course course = new Course();
            student.setStudentId(studentId);
            course.setCourseId(courseId);
            studentService.deleteStudentCourse(student, course);
            log.info("Student with id = {} deleted from the course with id = {}", studentId, courseId);
            return "Student deleted from the course";
        } catch (Exception e) {
            log.error("Exception during deleting student from the course, " +
                    "nested exception: {}", e.toString());
            throw e;
        }
    }
}
