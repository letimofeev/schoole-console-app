package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Group;

import java.util.List;

public interface GroupDao {

    void save(Group group);

    List<Group> findAll();

    List<Group> findAllWithStudentCountLessThanEqual(int studentCount);
}
