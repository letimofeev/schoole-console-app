package org.foxminded.springcourse.consoleapp;

import org.foxminded.springcourse.consoleapp.config.ApplicationConfig;
import org.foxminded.springcourse.consoleapp.dao.GroupDao;
import org.foxminded.springcourse.consoleapp.dao.StudentDao;
import org.foxminded.springcourse.consoleapp.manager.CommandManager;
import org.foxminded.springcourse.consoleapp.service.CommandHandlerMapper;
import org.foxminded.springcourse.consoleapp.service.SqlScriptRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        GroupDao groupDao = context.getBean(GroupDao.class);
        StudentDao studentDao = context.getBean(StudentDao.class);
        SqlScriptRunner scriptRunner = context.getBean(SqlScriptRunner.class);

        // scriptRunner.executeSqlScript("db-init/init.sql");

        CommandManager commandManager = context.getBean(CommandManager.class);
        String s = commandManager.processCommand("find all groups with students number >= 1");
        System.out.println(s);
    }
}
