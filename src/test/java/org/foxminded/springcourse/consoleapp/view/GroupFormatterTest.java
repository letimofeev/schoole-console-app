package org.foxminded.springcourse.consoleapp.view;

import org.foxminded.springcourse.consoleapp.model.Group;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupFormatterTest {

    private final GroupFormatter groupFormatter = new GroupFormatter();

    @Test
    void formatGroups_shouldReturnNotification_whenInputIsEmptyList() {
        List<Group> groups = Collections.emptyList();

        String expected = "No groups found\n";
        String actual = groupFormatter.formatGroups(groups);

        assertEquals(expected, actual);
    }

    @Test
    void formatGroups_shouldReturnFormattedGroups_whenInputIsGroupList() {
        List<Group> groups = List.of(new Group(123, "Korn"),
                new Group(111, "Slipknot"),
                new Group(532, "Queen"),
                new Group(423, "Pink Floyd"));

        String expected = "Group: id = 123, group name = Korn\n" +
                "Group: id = 111, group name = Slipknot\n" +
                "Group: id = 532, group name = Queen\n" +
                "Group: id = 423, group name = Pink Floyd\n";
        String actual = groupFormatter.formatGroups(groups);

        assertEquals(expected, actual);
    }

    @Test
    void formatGroup_shouldReturnFormattedGroup_whenInputIsGroup() {
        Group group = new Group(1000, "Disturbed");

        String expected = "Group: id = 1000, group name = Disturbed";
        String actual = groupFormatter.formatGroup(group);

        assertEquals(expected, actual);
    }
}
