package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.annotation.CommandMatching;
import org.foxminded.springcourse.consoleapp.annotation.PatternGroup;
import org.foxminded.springcourse.consoleapp.exception.CommandHandlerMapperException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


@TestInstance(PER_CLASS)
class CommandHandlerMapperTest {

    private CommandHandlerMapper handlerMapper;

    @BeforeAll
    void setUp() {
        CommandHandler commandHandler = new CommandHandler() {

            @CommandMatching(regex = "pattern$")
            String handleNoArgsCommand() {
                return "result";
            }

            @CommandMatching(regex = "pattern (\\d+)$")
            String handleOneArgCommand(@PatternGroup(1) int number) {
                return "result " + number;
            }

            @CommandMatching(regex = "pattern (\\w+) (\\d+) (\\w+)")
            String handleThreeArgsCommand(@PatternGroup(2) int group2,
                                          @PatternGroup(3) String group3,
                                          @PatternGroup(1) String group1) {
                return "result " + group1 + " " + group2 + " " + group3;
            }
        };
        handlerMapper = new CommandHandlerMapper(List.of(commandHandler));
    }

    @Test
    void applyCommandHandler_shouldReturnResult_whenInputIsMatchingPatternWithoutArguments() {
        String command = "pattern";

        String actual = handlerMapper.applyCommandHandler(command);
        String expected = "result";

        assertEquals(expected, actual);
    }

    @Test
    void applyCommandHandler_shouldReturnResult_whenInputIsMatchingPatternWithOneArgument() {
        String command = "pattern 99";

        String actual = handlerMapper.applyCommandHandler(command);
        String expected = "result 99";

        assertEquals(expected, actual);
    }

    @Test
    void applyCommandHandler_shouldReturnResult_whenInputIsMatchingPatternWithThreeArguments() {
        String command = "pattern group1 2 group3";

        String actual = handlerMapper.applyCommandHandler(command);
        String expected = "result group1 2 group3";

        assertEquals(expected, actual);
    }

    @Test
    void applyCommandHandler_shouldThrowException_whenInputIsNotMatchingCommand() {
        String command = "Not existing command";

        assertThrows(CommandHandlerMapperException.class, () -> handlerMapper.applyCommandHandler(command));
    }
}