package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.model.Group;

import java.util.List;

public interface GroupService {

    List<Group> findAllWithStudentCountLessThanEqual(int count);

    List<Group> findAll();
}
