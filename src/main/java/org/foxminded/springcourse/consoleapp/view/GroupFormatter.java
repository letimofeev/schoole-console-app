package org.foxminded.springcourse.consoleapp.view;

import org.foxminded.springcourse.consoleapp.model.Group;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupFormatter {

    public String formatGroups(List<Group> groups) {
        if (groups.isEmpty()) {
            return "No groups found\n";
        }
        StringBuilder formattedStudents = new StringBuilder();
        for (Group group : groups) {
            String formattedStudent = formatGroup(group);
            formattedStudents.append(formattedStudent);
            formattedStudents.append("\n");
        }
        return formattedStudents.toString();
    }

    public String formatGroup(Group group) {
        return String.format("Group: id = %d, group name = %s",
                group.getId(),
                group.getName());
    }
}
