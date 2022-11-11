package org.foxminded.springcourse.consoleapp.command;

import org.foxminded.springcourse.consoleapp.model.Group;
import org.foxminded.springcourse.consoleapp.service.GroupService;
import org.foxminded.springcourse.consoleapp.view.GroupFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
public class GroupCommand {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final GroupService groupService;
    private final GroupFormatter groupFormatter;

    public GroupCommand(GroupService groupService, GroupFormatter groupFormatter) {
        this.groupService = groupService;
        this.groupFormatter = groupFormatter;
    }

    @ShellMethod("Find all groups with number of students <= given")
    public String findAllGroupsWithStudentsCountLessThanEqual(@ShellOption("--students-count") int studentsCount) {
        try {
            List<Group> groups = groupService.findAllWithStudentCountLessThanEqual(studentsCount);
            return groupFormatter.formatGroups(groups);
        } catch (Exception e) {
            log.error("Exception during finding groups with students count less or equal than," +
                    " nested exception: {}", e.toString());
            throw e;
        }
    }

    @ShellMethod("Find all groups")
    public String findAllGroups() {
        try {
            List<Group> groups = groupService.findAll();
            return groupFormatter.formatGroups(groups);
        } catch (Exception e) {
            log.error("Exception during finding all groups, nested exception: {}", e.toString());
            throw e;
        }
    }
}
