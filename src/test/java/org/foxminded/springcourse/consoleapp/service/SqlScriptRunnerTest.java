package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.foxminded.springcourse.consoleapp.model.Student;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SqlScriptRunnerTest {

    private final ConnectionConfig connectionConfig = new ConnectionConfig("jdbc:h2:mem:scriptrunner;DB_CLOSE_DELAY=-1", "sa", "");


    private final SqlScriptRunner scriptRunner = new SqlScriptRunner(connectionConfig);

    @Test
    void executeSqlScript_shouldCreateTableAndInsert_whenScriptIsCreateTableAndInsert() throws SQLException {
        String scriptPath = "./src/test/create_students_table.sql";

        scriptRunner.executeSqlScript(scriptPath);

        try (Connection connection = DriverManager.getConnection(connectionConfig.getUrl(), "sa", "")) {
            Statement statement = connection.createStatement();
            statement.execute("select * from students");
            ResultSet resultSet = statement.getResultSet();

            resultSet.next();

            Student expected = new Student(10, 11, "Henry", "Aaa");
            Student actual = new Student(resultSet.getInt(1), resultSet.getInt(2),
                    resultSet.getString(3), resultSet.getString(4));

            assertEquals(expected, actual);
        }
    }
}
