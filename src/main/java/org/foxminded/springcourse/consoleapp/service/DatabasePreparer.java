package org.foxminded.springcourse.consoleapp.service;

import org.apache.commons.lang3.RandomUtils;
import org.foxminded.springcourse.consoleapp.dao.CourseDao;
import org.foxminded.springcourse.consoleapp.dao.GroupDao;
import org.foxminded.springcourse.consoleapp.dao.StudentDao;
import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DatabasePreparer {

    private final SqlScriptRunner scriptRunner;
    private final DataGenerator dataGenerator;
    private final StudentDao studentDao;
    private final GroupDao groupDao;
    private final CourseDao courseDao;

    public DatabasePreparer(SqlScriptRunner scriptRunner, DataGenerator dataGenerator,
                            StudentDao studentDao, GroupDao groupDao, CourseDao courseDao) {
        this.scriptRunner = scriptRunner;
        this.dataGenerator = dataGenerator;
        this.studentDao = studentDao;
        this.groupDao = groupDao;
        this.courseDao = courseDao;
    }

    public void prepareBase(String initialScriptPath) {
        scriptRunner.executeSqlScript(initialScriptPath);

        List<Group> groups = generateAndSaveGroups();
        List<Student> students = generateAndSaveStudents(groups);
        List<Course> courses = generateAndSaveCourses();

        assignStudentCourses(students, courses);
    }

    private List<Course> generateAndSaveCourses() {
        List<Course> courses = dataGenerator.generateCourses();
        courses.forEach(courseDao::save);
        return courses;
    }

    private List<Student> generateAndSaveStudents(List<Group> groups) {
        List<Student> students = dataGenerator.generateRandomStudents(200, groups);
        students.forEach(studentDao::save);
        return students;
    }

    private List<Group> generateAndSaveGroups() {
        List<Group> groups = dataGenerator.generateRandomGroups(10);
        groups.forEach(groupDao::save);
        return groups;
    }

    private void assignStudentCourses(List<Student> students, List<Course> courses) {
        for (Student student : students) {
            int studentId = student.getId();
            Set<Integer> used = new HashSet<>();
            for (int i = 0; i < RandomUtils.nextInt(1, 4); i++) {
                int courseId = dataGenerator.getRandomElement(courses).getId();
                if (!used.contains(courseId)) {
                    studentDao.addStudentCourse(studentId, courseId);
                }
                used.add(courseId);
            }
        }
    }
}
