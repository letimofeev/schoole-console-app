package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.dao.GroupDao;
import org.foxminded.springcourse.consoleapp.model.Group;
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
class GroupServiceImplTest {

    @Mock
    private GroupDao groupDao;

    @InjectMocks
    private GroupServiceImpl groupService;

    @BeforeAll
    void setUp() {
        openMocks(this);

        List<Group> groups = List.of(new Group("group1"), new Group("group2"), new Group("group111"));
        List<Group> allGroups = List.of(new Group("all-"));

        when(groupDao.findAllWithStudentCountLessThanEqual(1)).thenReturn(groups);
        when(groupDao.findAll()).thenReturn(allGroups);
    }

    @Test
    void findAllWithStudentCountLessThanEqual_shouldReturnGroupsFromDao_whenInputIs1() {
        int count = 1;

        List<Group> expected = List.of(new Group("group1"), new Group("group2"), new Group("group111"));
        List<Group> actual = groupService.findAllWithStudentCountLessThanEqual(count);

        assertEquals(expected, actual);
    }

    @Test
    void findAll_shouldReturnAllGroupsFromDao_whenGroupsExist() {
        List<Group> expected = List.of(new Group("all-"));
        List<Group> actual = groupService.findAll();

        assertEquals(expected, actual);
    }
}
