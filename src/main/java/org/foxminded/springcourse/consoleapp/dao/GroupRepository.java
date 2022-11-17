package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Query(value = "SELECT g\n" +
            "FROM Group g\n" +
            "JOIN Student s on g.groupId = s.groupId\n" +
            "GROUP BY g.groupId, g.groupName\n" +
            "HAVING count(*) <= :studentsCount")
    List<Group> findAllWithStudentCountLessThanEqual(@Param("studentsCount") long studentsCount);
}
