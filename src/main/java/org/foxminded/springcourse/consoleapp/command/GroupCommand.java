package org.foxminded.springcourse.consoleapp.command;

import org.foxminded.springcourse.consoleapp.model.Group;
import org.foxminded.springcourse.consoleapp.service.GroupService;
import org.foxminded.springcourse.consoleapp.view.GroupFormatter;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
public class GroupCommand {

    private final GroupService groupService;
    private final GroupFormatter groupFormatter;

    public GroupCommand(GroupService groupService, GroupFormatter groupFormatter) {
        this.groupService = groupService;
        this.groupFormatter = groupFormatter;
    }

    @ShellMethod("Find all groups with number of students <= given")
    public String findAllGroupsWithStudentCountLessThanEqual(@ShellOption("--student-count") int studentCount) {
        List<Group> groups = groupService.findAllWithStudentCountLessThanEqual(studentCount);
        return groupFormatter.formatGroups(groups);
    }

    @ShellMethod("Find all groups")
    public String findAllGroups() {
        List<Group> groups = groupService.findAll();
        return groupFormatter.formatGroups(groups);
    }
}
