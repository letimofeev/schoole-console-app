package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.exception.SqlScriptRunnerException;
import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class SqlScriptRunner {

    private final ConnectionConfig connectionConfig;

    public SqlScriptRunner(ConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    public void executeSqlScript(String path) {
        String script = getScript(path);
        try (Connection connection = createConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(script);
        } catch (SQLException e) {
            throw new SqlScriptRunnerException(e);
        }
    }

    private String getScript(String path) {
        StringBuilder script = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line);
            }
            return script.toString();
        } catch (IOException e) {
            throw new SqlScriptRunnerException(e);
        }
    }

    private Connection createConnection() throws SQLException {
        String url = connectionConfig.getUrl();
        String user = connectionConfig.getUser();
        String password = connectionConfig.getPassword();
        return DriverManager.getConnection(url, user, password);
    }
}
