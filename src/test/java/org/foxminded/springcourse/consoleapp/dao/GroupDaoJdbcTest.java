package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Group;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Testcontainers
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
class GroupDaoJdbcTest {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final GroupRowMapper rowMapper = new GroupRowMapper();

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:13.3")
            .withDatabaseName("school-test")
            .withInitScript("groups_test_tables.sql");

    @BeforeAll
    static void setUp() {
        container.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.driver-class-name", container::getDriverClassName);
    }

    @AfterAll
    static void tearDown() {
        container.stop();
    }

    @Test
    void save_shouldInjectId_whenStudentSaved() {
        Group group = new Group("Grup");
        groupDao.save(group);

        int unexpectedId = 0;
        int actualId = group.getGroupId();

        assertNotEquals(unexpectedId, actualId);
    }

    @Sql("classpath:groups_students_data.sql")
    @Test
    void findAll_shouldReturnExpectedRowsNumber_whenGroupsExist() {
        int expectedSize = 5;
        int actualSize = groupDao.findAll().size();

        assertEquals(expectedSize, actualSize);
    }

    @Sql("classpath:groups_students_data.sql")
    @Test
    void findAllWithStudentCountLessThanEqual_shouldReturnExpectedRowsNumber_whenGroupsExist() {
        int expectedSize = 2;
        int actualSize = groupDao.findAllWithStudentCountLessThanEqual(3).size();

        assertEquals(expectedSize, actualSize);
    }

    @Sql(statements = "INSERT INTO groups VALUES (1001, 'Bugatti')")
    @Test
    void findById_shouldReturnPresentOptional_whenCourseExists() {
        Group expected = new Group(1001, "Bugatti");
        Group actual = groupDao.findById(1001).get();

        assertEquals(expected, actual);
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenCourseExists() {
        Optional<Group> actual = groupDao.findById(1000);
        assertTrue(actual.isEmpty());
    }

    @Sql(statements = "INSERT INTO groups VALUES (1002, 'Ducati')")
    @Test
    void update_shouldUpdate_whenInputIsId() {
        Group expected = new Group(1002, "Bugatti");

        groupDao.update(expected);

        String query = "SELECT * FROM groups WHERE group_id = ?";
        Group actual = jdbcTemplate.query(query, rowMapper, 1002).get(0);

        assertEquals(expected, actual);
    }

    @Sql(statements = "INSERT INTO groups VALUES (11133, 'Ferrari')")
    @Test
    void deleteById_shouldDelete_whenInputIsId() {
        groupDao.deleteById(11133);

        String query = "SELECT * FROM groups WHERE group_id = ?";
        List<Group> actual = jdbcTemplate.query(query, rowMapper, 11133);

        assertTrue(actual.isEmpty());
    }
}