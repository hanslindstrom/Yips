package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ConnectionRepository {

    @Autowired
    DataSource dataSource;

    @Autowired
    UserRepository userRepository;



    public void saveUserGroupOwnerConnection(Group group, Long groupId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO usergroupconnection(USERID, GROUPID, USERROLE) VALUES(?,?,?)")){
            ps.setLong(1, group.getOwnerId());
            ps.setLong(2, groupId);
            ps.setString(3, "GroupAdmin");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void connectNewMemberToGroup(Group group, Long memberId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO usergroupconnection(USERID, GROUPID, USERROLE) VALUES(?,?,?)")){
            ps.setLong(1, memberId);
            ps.setLong(2, group.getId());
            ps.setString(3, "GroupMember");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void userWorkoutConnect(String username, Long workoutId) {
        User user = userRepository.findByUsername(username);
        Long userId = user.getId();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO userworkoutconnection(USERID, WORKOUTID) VALUES (?,?)")) {

            ps.setLong(1, userId);
            ps.setLong(2, workoutId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void userExerciseConnect(Long userId, Long exerciseId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO userexerciseconnection(USERID, EXERCISEID) VALUES (?,?)")) {
            ps.setLong(1, userId);
            ps.setLong(2, exerciseId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void groupWorkoutConnect(Long groupId, Long workoutId) {
        try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO groupworkoutconnection(GROUPID, WORKOUTID) VALUES (?,?)")) {
            ps.setLong(1, groupId);
            ps.setLong(2, workoutId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void workoutExerciseConnect(Long workoutId, Long exerciseId) {
        try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO workoutexerciseconnection(WORKOUTID, EXERCISEID) VALUES(?,?)")){
            ps.setLong(1, workoutId);
            ps.setLong(2, exerciseId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Exercise> findExercisesInWorkoutByWorkoutId (Long workoutId) {
        List<Exercise> exercises = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM exercise JOIN workoutexerciseconnection ON EXERCISE.ID=WORKOUTEXERCISECONNECTION.EXERCISEID WHERE WORKOUTID=?")){
            ps.setLong(1,workoutId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Exercise exercise = new Exercise();
                exercise.setId(rs.getLong("id"));
                exercise.setName(rs.getString("name"));
                exercise.setSeconds(rs.getInt("seconds"));
                exercise.setMeters(rs.getInt("meters"));
                exercise.setCalories(rs.getInt("calories"));
                exercise.setWeight(rs.getInt("weight"));
                exercise.setReps(rs.getInt("reps"));
                exercise.setSets(rs.getInt("sets"));
                exercise.setCadence(rs.getInt("cadence"));
                exercises.add(exercise);
            }
            } catch (SQLException e) {
            e.printStackTrace();
        }

        return exercises;
    }

    public List<Long> findGroupIdConnectedToWorkoutByWorkoutId(Long workoutId) {
        List<Long> groupIds = new ArrayList<>();
        Long groupId = null;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM GROUPWORKOUTCONNECTION WHERE WORKOUTID=?")) {
            ps.setLong(1, workoutId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                groupId = rs.getLong("groupid");
                groupIds.add(groupId);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groupIds;
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
