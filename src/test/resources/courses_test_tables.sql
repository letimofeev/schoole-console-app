CREATE TABLE courses
(
    course_id          SERIAL PRIMARY KEY,
    course_name        VARCHAR(30),
    course_description VARCHAR(50),

    CONSTRAINT uq_courses_course_name
        UNIQUE (course_name)
);
