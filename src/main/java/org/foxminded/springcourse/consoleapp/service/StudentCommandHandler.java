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

}
