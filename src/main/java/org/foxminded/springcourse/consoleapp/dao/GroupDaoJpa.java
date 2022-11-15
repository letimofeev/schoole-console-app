package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class GroupDaoJpa implements GroupDao {

    public static final String FIND_ALL = "FROM Group";
    public static final String FIND_ALL_WITH_LTE_STUDENTS = "SELECT g\n" +
            "FROM Group g\n" +
            "JOIN Student s on g.groupId = s.groupId\n" +
            "GROUP BY g.groupId, g.groupName\n" +
            "HAVING count(*) <= :studentsCount";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final EntityManager entityManager;

    public GroupDaoJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Group group) {
        entityManager.persist(group);

        log.debug("Group saved in table 'groups', values: group_id = {}, group_name = {}",
                group.getGroupId(), group.getGroupName());
    }

    @Override
    public List<Group> findAll() {
        TypedQuery<Group> query = entityManager.createQuery(FIND_ALL, Group.class);
        return query.getResultList();
    }

    @Override
    public List<Group> findAllWithStudentCountLessThanEqual(int studentsCount) {
        TypedQuery<Group> query = entityManager.createQuery(FIND_ALL_WITH_LTE_STUDENTS, Group.class);
        query.setParameter("studentsCount", (long) studentsCount);
        return query.getResultList();
    }

    @Override
    public Optional<Group> findById(int id) {
        Group group = entityManager.find(Group.class, id);
        if (group == null) {
            log.warn("Group with id = {} not found in table 'groups'", id);
        }
        return Optional.ofNullable(group);
    }

    @Override
    public void update(Group group) {
        entityManager.merge(group);

        log.debug("Group with id = {} updated in table 'groups', new values: group_name = {}",
                group.getGroupId(), group.getGroupName());
    }

    @Override
    public void delete(Group group) {
        group = entityManager.find(Group.class, group.getGroupId());
        if (group != null) {
            entityManager.remove(group);
            log.debug("Group with id = {} deleted from table 'groups'", group.getGroupId());
        }
    }
}
