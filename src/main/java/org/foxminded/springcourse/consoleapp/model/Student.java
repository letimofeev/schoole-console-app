package org.foxminded.springcourse.consoleapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

    private int studentId;
    private int groupId;
    private String firstName;
    private String lastName;

    public Student() {
    }

    public Student(int groupId, String firstName, String lastName) {
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student(int studentId, int groupId, String firstName, String lastName) {
        this.studentId = studentId;
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (studentId != student.studentId) return false;
        return groupId == student.groupId;
    }

    @Override
    public int hashCode() {
        int result = studentId;
        result = 31 * result + groupId;
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + studentId +
                ", groupId=" + groupId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
