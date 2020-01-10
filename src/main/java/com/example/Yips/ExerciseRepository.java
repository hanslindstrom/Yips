package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ExerciseRepository {
    @Autowired
    DataSource dataSource;

    @Autowired
    ConnectionRepository connectionRepository;

    @Autowired
    UserRepository userRepository;



    public List<String> findExerciseUserId(Long userId) {

        List<String>exerciseNames=new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT NAME FROM EXERCISE\n" +
                     "JOIN USEREXERCISECONNECTION ON \n" +
                     "EXERCISE.ID=USEREXERCISECONNECTION.EXERCISEID\n" +
                     "WHERE USERID=?")){
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String exerciseName = rs.getString("name");
                exerciseNames.add(exerciseName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exerciseNames;
    }

    public void addExercise(Exercise exercise, Long userId) {
        Long exerciseId=0L;
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO exercise(NAME, SECONDS, METERS, CALORIES, WEIGHT, REPS, SETS, CADENCE) VALUES(?,?,?,?,?,?,?,?)")) {

            ps.setString(1,exercise.getName());
            ps.setInt(2,exercise.getSeconds());
            ps.setInt(3, exercise.getMeters());
            ps.setInt(4, exercise.getCalories());
            ps.setInt(5, exercise.getWeight());
            ps.setInt(6, exercise.getReps());
            ps.setInt(7, exercise.getSets());
            ps.setInt(8, exercise.getCadence());
            ps.executeUpdate();
            /*try (Connection conn2 = dataSource.getConnection();
            PreparedStatement ps2 = conn2.prepareStatement("SELECT ID FROM EXERCISE WHERE NAME=?")){
                ps2.setString(1, exercise.getName());
                ResultSet rs2=ps2.executeQuery();
                if(rs2.next()) {
                    exerciseId= rs2.getLong("ID");
                }
            }*/
            exerciseId = findExerciseIdByExerciseName(exercise);
            connectionRepository.userExerciseConnect(userId, exerciseId);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public Long findExerciseIdByExerciseName (Exercise exercise) {
        Long exerciseId=0L;
        try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT ID FROM exercise WHERE NAME=?")){
            ps.setString(1,exercise.getName());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                exerciseId = rs.getLong("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exerciseId;
    }
}
