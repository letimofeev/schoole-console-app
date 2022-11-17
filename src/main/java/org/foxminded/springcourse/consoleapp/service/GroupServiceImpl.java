package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.dao.GroupRepository;
import org.foxminded.springcourse.consoleapp.model.Group;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<Group> findAllWithStudentCountLessThanEqual(int count) {
        return groupRepository.findAllWithStudentCountLessThanEqual(count);
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }
}
