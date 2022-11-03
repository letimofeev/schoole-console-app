package org.foxminded.springcourse.consoleapp.exception;

public class CommandHandlerMapperException extends RuntimeException {

    public CommandHandlerMapperException() {
    }

    public CommandHandlerMapperException(String message) {
        super(message);
    }

    public CommandHandlerMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandHandlerMapperException(Throwable cause) {
        super(cause);
    }
}
