package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WorkoutRepository {

    @Autowired
    DataSource dataSource;

    @Autowired
    ConnectionRepository connectionRepository;

    public List<Workout> findAll() {
        List<Workout> workouts = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM workout")){
            while(rs.next()) {
                long id = rs.getInt("id");
                String name = rs.getString("name");
                String category = rs.getString("category");

                Workout workout = new Workout();
                workout.setId(id);
                workout.setName(name);
                workout.setCategory(category);
                workouts.add(workout);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return workouts;
    }

    public Workout findByWorkoutname(String workoutName) {
        Workout workout = new Workout();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM workout WHERE name = ?")){
            ps.setString(1, workoutName);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                long id = rs.getInt("id");
                String name = rs.getString("name");
                String category = rs.getString("category");

                workout.setId(id);
                workout.setName(name);
                workout.setCategory(category);

            } else {
                workout=null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workout;
    }

    public void saveWorkout(Workout workout, User user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO workout(NAME, CATEGORY) VALUES(?,?)")){
            ps.setString(1,workout.getName());
            ps.setString(2, workout.getCategory());
            ps.executeUpdate();
            //connectionRepository.saveUserWorkoutConnection(workout, user.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //To add testgroups in DBinit....
    /*public void saveAll(List<Workout> workouts) {
        for(Workout workout:workouts) {
            saveWorkout(workout);
        }
    }*/

}
