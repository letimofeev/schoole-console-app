package org.foxminded.springcourse.consoleapp.manager;

import org.foxminded.springcourse.consoleapp.service.CommandHandlerMapper;
import org.springframework.stereotype.Component;

@Component
public class CommandManager {

    private final CommandHandlerMapper commandHandlerMapper;

    public CommandManager(CommandHandlerMapper commandHandlerMapper) {
        this.commandHandlerMapper = commandHandlerMapper;
    }

    public String processCommand(String command) {
        return commandHandlerMapper.applyCommandHandler(command);
    }
}
