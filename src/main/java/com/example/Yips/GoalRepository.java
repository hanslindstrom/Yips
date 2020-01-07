package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class GoalRepository {

    @Autowired
    private DataSource dataSource;

    public Goal getGoalByGroupId(Group group){
        Goal goal = new Goal();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM goals WHERE id = ?")){
            ps.setString(1, group.getId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("goalname");
                Date deadline = rs.getDate("deadline");
                Long groupid = rs.getLong("groupid");
                boolean completed = rs.getBoolean("completed");

                goal.setId(id);
                goal.setName(name);
                goal.setDeadline(deadline);

            } else {
                goal=null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goal;
    }
}
