CREATE TABLE groups
(
    group_id   SERIAL PRIMARY KEY,
    group_name VARCHAR(20),

    CONSTRAINT uq_groups_group_name
        UNIQUE (group_name)
);

CREATE TABLE students
(
    student_id SERIAL PRIMARY KEY,
    group_id   INTEGER,
    first_name VARCHAR(50),
    last_name  VARCHAR(50),

    CONSTRAINT fk_students_groups
        FOREIGN KEY (group_id)
            REFERENCES groups (group_id)
);

CREATE TABLE courses
(
    course_id          SERIAL PRIMARY KEY,
    course_name        VARCHAR(30),
    course_description VARCHAR(50),

    CONSTRAINT uq_courses_course_name
        UNIQUE (course_name)
);

CREATE TABLE students_courses
(
    student_id INT,
    course_id  INT,

    CONSTRAINT fk_students_courses_students
        FOREIGN KEY (student_id)
            REFERENCES students (student_id),

    CONSTRAINT fk_students_courses_courses
        FOREIGN KEY (course_id)
            REFERENCES courses (course_id),

    CONSTRAINT uq_students_courses_student_id_course_id
        UNIQUE (student_id, course_id)
);

INSERT INTO courses (course_name, course_description)
VALUES ('math', 'math_desc'),
       ('meth', 'meth_desc'),
       ('biology', 'biology_desc'),
       ('informatics', 'informatics_desc'),
       ('computer_science', 'computer_science_desc'),
       ('memelogy', 'memelogy_desc'),
       ('religion', 'religion_desc'),
       ('geometry', 'geometry_desc'),
       ('functional_analysis', 'functional_analysis_desc'),
       ('machine_learning', 'machine_learning_desc');

INSERT INTO groups (group_name)
VALUES ('group1'),
       ('group2'),
       ('group3'),
       ('group4'),
       ('group5');

INSERT INTO students (group_id, first_name, last_name)
VALUES (1, 'first_name1', 'last_name1'),
       (2, 'first_name2', 'last_name2'),
       (3, 'first_name3', 'last_name3'),
       (1, 'first_name4', 'last_name4'),
       (1, 'first_name4', 'last_name4'),
       (1, 'first_name5', 'last_name5'),
       (2, 'first_name6', 'last_name6'),
       (3, 'first_name7', 'last_name7');

INSERT INTO students_courses (student_id, course_id)
VALUES (1, 2),
       (1, 3),
       (2, 2),
       (2, 1),
       (3, 3),
       (2, 3);
