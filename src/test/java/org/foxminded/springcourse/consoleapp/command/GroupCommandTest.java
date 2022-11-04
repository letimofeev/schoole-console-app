package org.foxminded.springcourse.consoleapp.command;

import org.foxminded.springcourse.consoleapp.dao.GroupDao;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.foxminded.springcourse.consoleapp.view.GroupFormatter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@TestInstance(PER_CLASS)
class GroupCommandTest {

    @Mock
    private GroupDao groupDao;

    @Mock
    private GroupFormatter groupFormatter;

    @InjectMocks
    private GroupCommand command;

    @BeforeAll
    void setUp() {
        openMocks(this);

        List<Group> groups = List.of(new Group("group1"), new Group("group2"), new Group("group111"));
        List<Group> allGroups = List.of(new Group("all-"));

        when(groupDao.findAllWithStudentCountLessThanEqual(1)).thenReturn(groups);
        when(groupDao.findAll()).thenReturn(allGroups);

        when(groupFormatter.formatGroups(groups)).thenReturn("Formatted group1, group2, group111");
        when(groupFormatter.formatGroups(allGroups)).thenReturn("Formatted all groups");
    }

    @Test
    void findAllGroupsWithStudentCountLessThanEqual_shouldReturnFormattedGroups_whenInputIsId() {
        String expected = "Formatted group1, group2, group111";
        String actual = command.findAllGroupsWithStudentCountLessThanEqual(1);

        assertEquals(expected, actual);
    }

    @Test
    void findAllGroups_shouldReturnFormattedGroups_whenInputIsEmpty() {
        String expected = "Formatted all groups";
        String actual = command.findAllGroups();

        assertEquals(expected, actual);
    }
}