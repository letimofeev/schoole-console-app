package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao {

    void save(Group group);

    List<Group> findAll();

    List<Group> findAllWithStudentCountLessThanEqual(int studentsCount);

    Optional<Group> findById(int id);

    void update(Group group);

    void deleteById(int id);
}
