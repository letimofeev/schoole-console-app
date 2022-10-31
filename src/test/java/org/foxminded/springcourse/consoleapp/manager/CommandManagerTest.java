package org.foxminded.springcourse.consoleapp.manager;

import org.foxminded.springcourse.consoleapp.service.CommandHandlerMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@TestInstance(PER_CLASS)
class CommandManagerTest {

    @InjectMocks
    private CommandManager commandManager;

    @Mock
    private CommandHandlerMapper commandHandlerMapper;

    @BeforeAll
    void setUp() {
        openMocks(this);
        when(commandHandlerMapper.applyCommandHandler(anyString())).thenReturn("COMMAND MAPPER RESULT");
        when(commandHandlerMapper.applyCommandHandler(null)).thenThrow(new NullPointerException());
    }

    @Test
    void processCommand_shouldApplyCommandHandlerMapper_whenInputIsCommand() {
        String command = "command";

        String expected = "COMMAND MAPPER RESULT";
        String actual = commandManager.processCommand(command);

        assertEquals(expected, actual);
    }

    @Test
    void processCommand_shouldReturnErrorMessage_whenInputIsNull() {
        String command = null;

        String expected = "Unexpected error: null";
        String actual = commandManager.processCommand(command);

        assertEquals(expected, actual);
    }
}
