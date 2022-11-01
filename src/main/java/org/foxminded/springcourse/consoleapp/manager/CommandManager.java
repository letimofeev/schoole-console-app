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
        String result;
        try {
            result = commandHandlerMapper.applyCommandHandler(command);
        } catch (Exception e) {
            result = String.format("Unexpected error: %s", e.getMessage());
        }
        return result;
    }
}
