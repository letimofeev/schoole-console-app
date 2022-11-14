package org.foxminded.springcourse.consoleapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupId;

    @Column
    private String groupName;

    public Group() {
    }

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group(int groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
