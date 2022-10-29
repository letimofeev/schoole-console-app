package org.foxminded.springcourse.consoleapp.model;

import lombok.Getter;
import lombok.Setter;
import org.foxminded.springcourse.consoleapp.annotation.Column;
import org.foxminded.springcourse.consoleapp.annotation.Id;
import org.foxminded.springcourse.consoleapp.annotation.Table;

@Getter
@Setter
@Table(name = "courses")
public class Course {

    @Id(autogenerated = true)
    @Column(name = "course_id")
    private int id;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "course_description")
    private String courseDescription;
}