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
    last_name  VARCHAR(50)
);
