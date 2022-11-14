package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentDaoJpa implements StudentDao {

    public static final String FIND_ALL = "FROM Student";
    public static final String FIND_ALL_BY_COURSE_NAME = "SELECT s.student_id, group_id, first_name, last_name\n" +
            "FROM students s\n" +
            "JOIN students_courses sc on s.student_id = sc.student_id\n" +
            "JOIN courses c on c.course_id = sc.course_id\n" +
            "WHERE c.course_name = :courseName";
    public static final String DELETE_BY_ID = "DELETE FROM Student WHERE studentId = :studentId";
    public static final String ADD_STUDENT_COURSE = "INSERT INTO students_courses (student_id, course_id) VALUES (:studentId, :courseId)";
    public static final String DELETE_STUDENT_COURSE = "DELETE FROM students_courses WHERE student_id = :studentId AND course_id = :courseId";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final EntityManager entityManager;

    public StudentDaoJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Student student) {
        entityManager.persist(student);

        log.debug("Student saved in table 'students', values: student_id = {}, " +
                        "group_id = {}, first_name = {}, last_name = {}",
                student.getStudentId(), student.getGroupId(),
                student.getFirstName(), student.getLastName());
    }

    @Override
    public List<Student> findAll() {
        TypedQuery<Student> query = entityManager.createQuery(FIND_ALL, Student.class);
        return query.getResultList();
    }

    @Override
    public List<Student> findAllByCourseName(String courseName) {
        Query query = entityManager.createNativeQuery(FIND_ALL_BY_COURSE_NAME, Student.class);
        query.setParameter("courseName", courseName);
        return query.getResultList();
    }

    @Override
    public Optional<Student> findById(int id) {
        Student student = entityManager.find(Student.class, id);
        if (student == null) {
            log.warn("Student with id = {} not found in table 'students'", id);
        }
        return Optional.ofNullable(student);
    }

    @Override
    public void update(Student student) {
        entityManager.merge(student);

        log.debug("Student with id = {} updated in table 'students', new values: group_id = {}, " +
                "first_name = {}, last_name = {}", student.getStudentId(), student.getGroupId(),
                student.getFirstName(), student.getLastName());
    }

    @Override
    public void deleteById(int id) {
        Query query = entityManager.createQuery(DELETE_BY_ID);
        query.setParameter("studentId", id);

        log.debug("Student with id = {} deleted from table 'students'", id);
    }

    @Override
    public void addStudentCourse(int studentId, int courseId) {
        Query query = entityManager.createNativeQuery(ADD_STUDENT_COURSE);
        query.setParameter("studentId", studentId);
        query.setParameter("courseId", courseId);
        query.executeUpdate();

        log.debug("Student with id = {} added to the course with id = {} in table 'students_courses'",
                studentId, courseId);
    }

    @Override
    public void deleteStudentCourse(int studentId, int courseId) {
        Query query = entityManager.createNativeQuery(DELETE_STUDENT_COURSE);
        query.setParameter("studentId", studentId);
        query.setParameter("courseId", courseId);
        query.executeUpdate();

        log.debug("Student with id = {} deleted from the course with id = {} in table 'students_courses'",
                studentId, courseId);
    }
}
