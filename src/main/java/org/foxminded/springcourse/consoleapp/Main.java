package org.foxminded.springcourse.consoleapp;

import org.foxminded.springcourse.consoleapp.config.DatabaseConfig;
import org.foxminded.springcourse.consoleapp.dao.GroupDao;
import org.foxminded.springcourse.consoleapp.dao.StudentDao;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        GroupDao groupDao = context.getBean(GroupDao.class);
        StudentDao studentDao = context.getBean(StudentDao.class);

        List<Group> groups = groupDao.findAllWithStudentCountLessThanEqual(2);
        System.out.println(groups);

        Student student = new Student();
        student.setFirstName("Bob");
        student.setLastName("Martin");
        student.setGroupId(1);
        studentDao.save(student);
        System.out.println(student);
    }
}
