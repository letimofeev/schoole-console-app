package org.foxminded.springcourse.consoleapp.service;

import org.foxminded.springcourse.consoleapp.dao.CourseDao;
import org.foxminded.springcourse.consoleapp.model.Course;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;

    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public List<Course> findAll() {
        return courseDao.findAll();
    }
}
