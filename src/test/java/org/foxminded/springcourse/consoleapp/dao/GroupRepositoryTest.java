package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Group;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Testcontainers
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private EntityManager entityManager;

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
        groupRepository.save(group);

        int unexpectedId = 0;
        int actualId = group.getGroupId();

        assertNotEquals(unexpectedId, actualId);
    }

    @Sql("classpath:groups_students_data.sql")
    @Test
    void findAll_shouldReturnExpectedRowsNumber_whenGroupsExist() {
        int expectedSize = 5;
        int actualSize = groupRepository.findAll().size();

        assertEquals(expectedSize, actualSize);
    }

    @Sql("classpath:groups_students_data.sql")
    @Test
    void findAllWithStudentCountLessThanEqual_shouldReturnExpectedRowsNumber_whenGroupsExist() {
        int expectedSize = 2;
        int actualSize = groupRepository.findAllWithStudentCountLessThanEqual(3).size();

        assertEquals(expectedSize, actualSize);
    }

    @Sql(statements = "INSERT INTO groups VALUES (1001, 'Bugatti')")
    @Test
    void findById_shouldReturnPresentOptional_whenCourseExists() {
        Group expected = new Group(1001, "Bugatti");
        Group actual = groupRepository.findById(1001).get();

        assertEquals(expected, actual);
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenCourseNotExists() {
        Optional<Group> actual = groupRepository.findById(1000);

        assertTrue(actual.isEmpty());
    }

    @Sql(statements = "INSERT INTO groups VALUES (11133, 'Ferrari')")
    @Test
    void delete_shouldDelete_whenInputIsGroup() {
        Group group = new Group();
        group.setGroupId(11133);

        groupRepository.delete(group);

        Group actual = entityManager.find(Group.class, 11133);

        assertNull(actual);
    }
}