INSERT INTO courses (course_name, course_description)
VALUES ('course1', 'course1desc'),
       ('course2', 'course2desc'),
       ('course3', 'course3desc'),
       ('course4', 'course4desc'),
       ('course5', 'course5desc'),
       ('course6', 'course6desc');

INSERT INTO students (group_id, first_name, last_name)
VALUES (1, 'first_name1', 'last_name1'),
       (2, 'first_name2', 'last_name2'),
       (3, 'first_name3', 'last_name3'),
       (1, 'first_name4', 'last_name4'),
       (1, 'first_name5', 'last_name5'),
       (1, 'first_name6', 'last_name6'),
       (2, 'first_name7', 'last_name7'),
       (3, 'first_name8', 'last_name8');

INSERT INTO students_courses (student_id, course_id)
VALUES (1, 2),
       (1, 3),
       (2, 2),
       (2, 1),
       (5, 3),
       (2, 3);
