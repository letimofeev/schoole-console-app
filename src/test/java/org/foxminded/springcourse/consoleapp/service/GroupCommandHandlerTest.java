package org.foxminded.springcourse.consoleapp.service;

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
class GroupCommandHandlerTest {

    @Mock
    private GroupDao groupDao;

    @Mock
    private GroupFormatter groupFormatter;

    @InjectMocks
    private GroupCommandHandler commandHandler;

    @BeforeAll
    void setUp() {
        openMocks(this);

        List<Group> groups = List.of(new Group("group1"), new Group("group2"), new Group("group111"));
        when(groupDao.findAllWithStudentCountLessThanEqual(1)).thenReturn(groups);
        when(groupFormatter.formatGroups(groups)).thenReturn("Formatted group1, group2, group111");
    }

    @Test
    void findAllWithLessThanEqualStudentCount_shouldReturnFormattedGroups_whenInputIsId() {
        String expected = "Formatted group1, group2, group111";
        String actual = commandHandler.findAllWithLessThanEqualStudentCount(1);

        assertEquals(expected, actual);
    }
}