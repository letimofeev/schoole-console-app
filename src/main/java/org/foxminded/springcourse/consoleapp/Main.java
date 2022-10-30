package org.foxminded.springcourse.consoleapp;

import org.apache.commons.lang3.RandomUtils;
import org.foxminded.springcourse.consoleapp.config.ApplicationConfig;
import org.foxminded.springcourse.consoleapp.dao.CourseDao;
import org.foxminded.springcourse.consoleapp.dao.GroupDao;
import org.foxminded.springcourse.consoleapp.dao.StudentDao;
import org.foxminded.springcourse.consoleapp.manager.CommandManager;
import org.foxminded.springcourse.consoleapp.model.Course;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.foxminded.springcourse.consoleapp.service.DataGenerator;
import org.foxminded.springcourse.consoleapp.service.DatabasePreparer;
import org.foxminded.springcourse.consoleapp.service.SqlScriptRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        DatabasePreparer databasePreparer = context.getBean(DatabasePreparer.class);
        databasePreparer.prepareBase("db-init/init.sql");

        CommandManager commandManager = context.getBean(CommandManager.class);

        Scanner scanner = new Scanner(System.in);
        String exitCommand = "exit";

        String command;
        while (!(command = scanner.nextLine()).equalsIgnoreCase(exitCommand)) {
            String result = commandManager.processCommand(command);
            System.out.println(result);
        }
    }
}
