package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.dao.StudentRepository;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void deleteById(int id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> findAllByCourseName(String courseName) {
        return studentRepository.findAllByCourseName(courseName);
    }

    @Override
    public void addStudentCourse(int studentId, int courseId) {
        studentRepository.addStudentCourse(studentId, courseId);
    }

    @Override
    public void deleteStudentCourse(int studentId, int courseId) {
        studentRepository.deleteStudentCourse(studentId, courseId);
    }
}
