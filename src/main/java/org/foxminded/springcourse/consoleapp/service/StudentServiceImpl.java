package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.dao.StudentDao;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
    public void deleteById(int id) {
        studentDao.deleteById(id);
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
    public void addStudentCourse(int studentId, int courseId) {
        studentDao.addStudentCourse(studentId, courseId);
    }

    @Override
    public void deleteStudentCourse(int studentId, int courseId) {
        studentDao.deleteStudentCourse(studentId, courseId);
    }
}
