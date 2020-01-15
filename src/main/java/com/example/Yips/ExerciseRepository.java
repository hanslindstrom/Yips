package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
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


        String[] exerciseNameArray = exercise.getName().split(",");
        System.out.println("size"+exerciseNameArray.length);
        for(String exName:exerciseNameArray){
            System.out.println(exName);
        }
        String exName="";
        if(exerciseNameArray.length==1) {
            exName=exerciseNameArray[0];
        } else {
            exName=exerciseNameArray[1];
        }
        exercise.setName(exName);

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
            exerciseId = findExerciseIdByExercise(exercise);
            connectionRepository.userExerciseConnect(userId, exerciseId);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public Long findExerciseIdByExercise (Exercise exercise) {
        Long exerciseId=0L;
        List<Long>exerciseIdList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT ID FROM exercise WHERE NAME=?")){
            ps.setString(1,exercise.getName());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Long exerciseIdDB = rs.getLong("ID");
                exerciseIdList.add(exerciseIdDB);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(Long id:exerciseIdList) {
            if(id>exerciseId) {
                exerciseId=id;
            }
        }
        return exerciseId;
    }

    public Long findExerciseIdByExerciseName (String exerciseName) {
        Long exerciseId=0L;
        List<Long>exerciseIdList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT ID FROM exercise WHERE NAME=?")){
            ps.setString(1, exerciseName);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Long exerciseIdDB = rs.getLong("ID");
                exerciseIdList.add(exerciseIdDB);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(Long id:exerciseIdList) {
            if(id>exerciseId) {
                exerciseId=id;
            }
        }
        return exerciseId;
    }


    public Exercise findExerciseById(Long exerciseId) {
        Exercise exercise = new Exercise();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM exercise WHERE ID=?")){
            ps.setLong(1,exerciseId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exercise.setId(rs.getLong("ID"));
                exercise.setName(rs.getString("NAME"));
                exercise.setMeters(rs.getInt("METERS"));
                exercise.setCalories(rs.getInt("CALORIES"));
                exercise.setWeight(rs.getInt("WEIGHT"));
                exercise.setReps(rs.getInt("REPS"));
                exercise.setSets(rs.getInt("SETS"));
                exercise.setCadence(rs.getInt("CADENCE"));
                ps.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exercise;

    }

    // TODO update exercise
    public void updateExercise(Exercise exercise) {
        System.out.println("Inside update exercise method");
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE exercise SET NAME='"+exercise.getName()
                            +"', SECONDS='"+exercise.getSeconds()
                            +"', METERS='" +exercise.getMeters()+"', CALORIES='"+exercise.getCalories()
                            +"', WEIGHT='"+exercise.getWeight()+"', REPS='"+exercise.getReps()
                            +"', SETS='"+exercise.getSets()+"', CADENCE='"+exercise.getCadence()+"' WHERE ID="+exercise.getId())) {

            ps.executeUpdate();
            System.out.println("executed update exercise metod, with name " + exercise.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
