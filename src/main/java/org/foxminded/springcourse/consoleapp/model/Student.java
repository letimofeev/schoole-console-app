package org.foxminded.springcourse.consoleapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

    private int id;
    private int groupId;
    private String firstName;
    private String lastName;

    public Student() {
    }

    public Student(int id, int groupId, String firstName, String lastName) {
        this.id = id;
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != student.id) return false;
        return groupId == student.groupId;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + groupId;
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
