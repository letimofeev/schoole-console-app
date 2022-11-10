package org.foxminded.springcourse.consoleapp.view;

import org.foxminded.springcourse.consoleapp.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GroupFormatter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public String formatGroups(List<Group> groups) {
        if (groups.isEmpty()) {
            log.warn("Empty courses list passed to formatter");
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
