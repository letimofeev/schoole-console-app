package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.dao.GroupDao;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupDao groupDao;

    public GroupServiceImpl(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public List<Group> findAllWithStudentCountLessThanEqual(int count) {
        return groupDao.findAllWithStudentCountLessThanEqual(count);
    }

    @Override
    public List<Group> findAll() {
        return groupDao.findAll();
    }
}
