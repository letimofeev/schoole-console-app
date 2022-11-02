package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

            assertTrue(resultSet.next());

            int expectedId = 10;
            String expectedName = "Henry";

            assertEquals(expectedId, resultSet.getInt(1));
            assertEquals(expectedName, resultSet.getString(2));
        }
    }
}