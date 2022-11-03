package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.dto.EntityMetaDataCache;
import org.foxminded.springcourse.consoleapp.manager.EntityMetaDataManager;
import org.foxminded.springcourse.consoleapp.model.ConnectionConfig;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.foxminded.springcourse.consoleapp.service.EntityDataMapper;
import org.foxminded.springcourse.consoleapp.service.EntityMetaDataExtractor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GroupDaoTest {

    private final ConnectionConfig connectionConfig = new ConnectionConfig("jdbc:h2:mem:school;DB_CLOSE_DELAY=-1", "sa", "");

    private final EntityMetaDataManager metaDataManager = new EntityMetaDataManager(new EntityMetaDataCache(), new EntityMetaDataExtractor());
    private final CrudQueryBuilder<Group, Integer> queryBuilder = new CrudQueryBuilderPostgres<>(metaDataManager);
    private final EntityDataMapper<Group> dataMapper = new EntityDataMapper<>(metaDataManager);

    private final GroupDao groupDao = new GroupDao(connectionConfig, queryBuilder, dataMapper);

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionConfig.getUrl(), "sa", "")) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE groups \n" +
                    "(\n" +
                    "  group_id INTEGER AUTO_INCREMENT,\n" +
                    "  group_name VARCHAR(20)\n" +
                    ");");
            statement.execute("CREATE TABLE students\n" +
                    "(\n" +
                    "    student_id INTEGER AUTO_INCREMENT,\n" +
                    "    group_id   INTEGER,\n" +
                    "    first_name VARCHAR(20),\n" +
                    "    last_name  VARCHAR(20)\n" +
                    ");");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionConfig.getUrl(), "sa", "")) {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS groups;");
            statement.execute("DROP TABLE IF EXISTS students;");
        }
    }

    @Test
    void save_shouldInjectId_whenGroupSaved() {
        Group group = new Group("name");
        groupDao.save(group);

        int expectedId = 1;
        int actualId = group.getId();

        assertEquals(expectedId, actualId);
    }

    @Test
    void findById_shouldReturnEmpty_whenGroupDoesNotExist() {
        int id = -1;

        Optional<Group> actual = groupDao.findById(id, Group.class);

        assertTrue(actual.isEmpty());
    }

    @Test
    void findById_shouldReturnActual_whenGroupExists() {
        Group group = new Group("name");
        groupDao.save(group);

        Group expected = new Group(1, "name");
        Optional<Group> actualOptional = groupDao.findById(group.getId(), Group.class);

        assertEquals(expected, actualOptional.get());
    }

    @Test
    void findAll_shouldReturnEmpty_whenCoursesDoNotExist() {
        List<Group> actual = groupDao.findAll(Group.class);

        assertTrue(actual.isEmpty());
    }

    @Test
    void findAll_shouldReturnActual_whenCoursesExist() {
        for (int i = 1; i <= 100; i++) {
            Group group = new Group("name" + i);
            groupDao.save(group);
        }

        List<Group> actual = groupDao.findAll(Group.class);

        for (int i = 0; i < 100; i++) {
            int id = i + 1;
            Group expectedGroup = new Group(id, "name" + id);
            Group actualGroup = actual.get(i);
            assertEquals(expectedGroup, actualGroup);
        }
    }

    @Test
    void update_shouldUpdate_whenCourseUpdated() {
        Group group = new Group("name");
        groupDao.save(group);

        group.setName("updatedName");
        groupDao.update(group);

        Group expected = new Group(1, "updatedName");
        Optional<Group> actualOptional = groupDao.findById(group.getId(), Group.class);

        assertEquals(expected, actualOptional.get());
    }

    @Test
    void deleteById_shouldDelete_whenCourseExists() {
        Group group = new Group("methGroup");
        groupDao.save(group);

        groupDao.deleteById(1, Group.class);

        assertTrue(groupDao.findById(1, Group.class).isEmpty());
    }

    @Test
    void findAllWithStudentCountLessThanEqual_shouldReturnExpected_whenInputIsStudentCount() throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionConfig.getUrl(), "sa", "")) {
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO groups (group_name)\n" +
                    "VALUES ('group1'),\n" +
                    "       ('group2'),\n" +
                    "       ('group3'),\n" +
                    "       ('group4'),\n" +
                    "       ('group5');");
            statement.execute("INSERT INTO students (group_id, first_name, last_name)\n" +
                    "VALUES (1, 'first_name1', 'last_name1'),\n" +
                    "       (2, 'first_name2', 'last_name2'),\n" +
                    "       (3, 'first_name3', 'last_name3'),\n" +
                    "       (1, 'first_name4', 'last_name4'),\n" +
                    "       (1, 'first_name4', 'last_name4'),\n" +
                    "       (2, 'first_name5', 'last_name5'),\n" +
                    "       (2, 'first_name6', 'last_name6'),\n" +
                    "       (3, 'first_name7', 'last_name7');");
        }

        List<Group> expected = List.of(new Group(1, "group1"), new Group(2, "group2"));
        List<Group> actual = groupDao.findAllWithStudentCountLessThanEqual(3);

        assertEquals(expected, actual);
    }
}
