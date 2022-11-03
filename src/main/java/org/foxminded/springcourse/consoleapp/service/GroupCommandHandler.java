package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.annotation.PatternGroup;
import org.foxminded.springcourse.consoleapp.annotation.CommandMatching;
import org.foxminded.springcourse.consoleapp.dao.GroupDao;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.foxminded.springcourse.consoleapp.view.GroupFormatter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupCommandHandler extends CommandHandler {

    private final GroupDao groupDao;
    private final GroupFormatter groupFormatter;

    public GroupCommandHandler(GroupDao groupDao, GroupFormatter groupFormatter) {
        this.groupDao = groupDao;
        this.groupFormatter = groupFormatter;
    }

    @CommandMatching(regex = "Find all groups with students number <= (\\d+)")
    public String findAllWithLessThanEqualStudentCount(@PatternGroup(1) int studentCount) {
        List<Group> groups = groupDao.findAllWithStudentCountLessThanEqual(studentCount);
        return groupFormatter.formatGroups(groups);
    }
}
