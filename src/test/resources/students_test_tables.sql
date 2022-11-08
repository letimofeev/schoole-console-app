CREATE TABLE students
(
    student_id SERIAL PRIMARY KEY,
    group_id   INTEGER,
    first_name VARCHAR(50),
    last_name  VARCHAR(50)
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

    CONSTRAINT uq_students_courses_student_id_course_id
        UNIQUE (student_id, course_id)
);
