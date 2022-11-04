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