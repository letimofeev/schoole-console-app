package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseDaoJpa implements CourseDao {

    public static final String FIND_ALL = "FROM Course";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final EntityManager entityManager;

    public CourseDaoJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Course course) {
        entityManager.persist(course);

        log.debug("Course saved in table 'courses', values: course_id = {}, course_name = {}, course_description = {}",
                course.getCourseId(), course.getCourseName(), course.getCourseDescription());
    }

    @Override
    public List<Course> findAll() {
        TypedQuery<Course> query = entityManager.createQuery(FIND_ALL, Course.class);
        return query.getResultList();
    }

    @Override
    public Optional<Course> findById(int id) {
        Course course = entityManager.find(Course.class, id);
        if (course == null) {
            log.warn("Course with id = {} not found in table 'courses'", id);
        }
        return Optional.ofNullable(course);
    }

    @Override
    public void update(Course course) {
        entityManager.merge(course);

        log.debug("Course with id = {} updated in table 'courses', new values: course_name = {}, " +
                "course_description = {}", course.getCourseId(),
                course.getCourseName(), course.getCourseDescription());
    }

    @Override
    public void delete(Course course) {
        course = entityManager.find(Course.class, course.getCourseId());
        if (course != null) {
            entityManager.remove(course);
            log.debug("Course with id = {} deleted from table 'courses'", course.getCourseId());
        }
    }
}
