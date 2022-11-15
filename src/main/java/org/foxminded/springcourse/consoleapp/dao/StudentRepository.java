package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query(value = "SELECT s.student_id, group_id, first_name, last_name\n" +
            "FROM students s\n" +
            "JOIN students_courses sc on s.student_id = sc.student_id\n" +
            "JOIN courses c on c.course_id = sc.course_id\n" +
            "WHERE c.course_name = :courseName", nativeQuery = true)
    List<Student> findAllByCourseName(@Param("courseName") String courseName);

    @Modifying
    @Query(value = "INSERT INTO students_courses (student_id, course_id) " +
            "VALUES (:studentId, :courseId)", nativeQuery = true)
    void addStudentCourse(@Param("studentId") int studentId, @Param("courseId") int courseId);

    @Modifying
    @Query(value = "DELETE FROM students_courses " +
            "WHERE student_id = :studentId AND course_id = :courseId", nativeQuery = true)
    void deleteStudentCourse(@Param("studentId") int studentId, @Param("courseId") int courseId);
}
