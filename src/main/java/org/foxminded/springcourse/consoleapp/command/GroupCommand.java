package org.foxminded.springcourse.consoleapp.command;

import org.foxminded.springcourse.consoleapp.dao.GroupDao;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.foxminded.springcourse.consoleapp.view.GroupFormatter;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
public class GroupCommand {

    private final GroupDao groupDao;
    private final GroupFormatter groupFormatter;

    public GroupCommand(GroupDao groupDao, GroupFormatter groupFormatter) {
        this.groupDao = groupDao;
        this.groupFormatter = groupFormatter;
    }

    @ShellMethod("Find all groups with number of students <= given")
    public String findAllGroupsWithStudentCountLessThanEqual(@ShellOption("--student-count") int studentCount) {
        List<Group> groups = groupDao.findAllWithStudentCountLessThanEqual(studentCount);
        return groupFormatter.formatGroups(groups);
    }

    @ShellMethod("Find all groups")
    public String findAllGroups() {
        List<Group> groups = groupDao.findAll();
        return groupFormatter.formatGroups(groups);
    }
}
