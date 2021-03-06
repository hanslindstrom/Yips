package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ConnectionRepository {

    @Autowired
    DataSource dataSource;

    public void saveUserGroupConnection(Group group, Long userId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO usergroupconnection(USERID, GROUPID, USERROLE) VALUES(?,?,?)")){
            ps.setLong(1, group.getOwnerId());
            ps.setLong(2, userId);
            ps.setString(3, "GroupAdmin");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   /* public void saveUserWorkoutConnection(Workout workout, Long userId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO USERWORKOUTCONNECTION(USERID, WORKOUTID) VALUES(?,?)")){
            ps.setLong(1, userId);
            ps.setLong(2, workout.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/




}
