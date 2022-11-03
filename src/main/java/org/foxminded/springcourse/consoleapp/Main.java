package org.foxminded.springcourse.consoleapp;

import org.foxminded.springcourse.consoleapp.config.ApplicationConfig;
import org.foxminded.springcourse.consoleapp.manager.CommandManager;
import org.foxminded.springcourse.consoleapp.service.DatabasePreparer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

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
