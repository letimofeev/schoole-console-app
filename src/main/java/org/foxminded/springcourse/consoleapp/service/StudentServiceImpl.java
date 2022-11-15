package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.dao.StudentDao;
import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public void save(Student student) {
        studentDao.save(student);
    }

    @Override
    public void delete(Student student) {
        studentDao.delete(student);
    }

    @Override
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Override
    public List<Student> findAllByCourseName(String courseName) {
        return studentDao.findAllByCourseName(courseName);
    }

    @Override
    public void addStudentCourse(Student student, Course course) {
        studentDao.addStudentCourse(student, course);
    }

    @Override
    public void deleteStudentCourse(Student student, Course course) {
        studentDao.deleteStudentCourse(student, course);
    }
}
