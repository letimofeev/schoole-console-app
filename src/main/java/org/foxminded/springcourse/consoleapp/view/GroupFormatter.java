package org.foxminded.springcourse.consoleapp.view;

import org.foxminded.springcourse.consoleapp.model.Group;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GroupFormatter {

    public String formatGroups(List<Group> groups) {
        if (groups.isEmpty()) {
            return "No groups found";
        }
        return groups.stream()
                .map(this::formatGroup)
                .collect(Collectors.joining("\n"));
    }

    public String formatGroup(Group group) {
        return String.format("Group: id = %d, group name = %s",
                group.getGroupId(),
                group.getGroupName());
    }
}
