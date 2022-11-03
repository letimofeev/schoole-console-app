package org.foxminded.springcourse.consoleapp.exception;

public class SqlScriptRunnerException extends RuntimeException {

    public SqlScriptRunnerException() {
    }

    public SqlScriptRunnerException(String message) {
        super(message);
    }

    public SqlScriptRunnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlScriptRunnerException(Throwable cause) {
        super(cause);
    }
}
