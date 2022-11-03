package org.foxminded.springcourse.consoleapp.exception;

public class EntityDataMapperException extends RuntimeException {

    public EntityDataMapperException() {
    }

    public EntityDataMapperException(String message) {
        super(message);
    }

    public EntityDataMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityDataMapperException(Throwable cause) {
        super(cause);
    }
}
