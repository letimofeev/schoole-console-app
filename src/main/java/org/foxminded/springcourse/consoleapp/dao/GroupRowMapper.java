package org.foxminded.springcourse.consoleapp.dao;

import org.foxminded.springcourse.consoleapp.model.Group;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupRowMapper implements RowMapper<Group> {

    @Override
    public Group mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Group group = new Group();
        group.setGroupId(resultSet.getInt("group_id"));
        group.setGroupName(resultSet.getString("group_name"));

        return group;
    }
}
