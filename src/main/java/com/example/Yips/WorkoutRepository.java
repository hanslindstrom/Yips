package com.example.Yips;

import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
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
    public List<Workout>workoutDateList () {
        List<Workout>workoutDateList = new ArrayList<>();
        Workout workout = new Workout();
        try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM workout")) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                long id = rs.getInt("id");
                Date date = rs.getDate("WDATE");
                if(date == null)
                    continue; //Sometimes empty workouts are created, and this becomes a null pointer exception
                workout.setId(id);
                LocalDate lDate=date.toLocalDate();

                workout.setDate(lDate);
                workoutDateList.add(workout);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workoutDateList;
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
            e.printStackTrace();
        }
        return workout;
    }
    public Long initiateWorkout() {
        Workout workout = new Workout();
        workout.setName("workoutinit"+Math.random());
        try(Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO workout(NAME, WDATE, WTYPE, WTIME, PLACE, DESCRIPTION, CATEGORY) VALUES(?,?,?,?,?,?,?)")) {
            ps.setString(1,workout.getName());
            ps.setDate(2,null);
            ps.setString(3, null);
            ps.setInt(4, 0);
            ps.setString(4, null);
            ps.setString(5, null);
            ps.setString(6, null);
            ps.setString(7, null);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Workout workoutDb=findByWorkoutname(workout.getName());
        return workoutDb.getId();
    }
    public void updateWorkout(Workout workout) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE workout SET NAME='"+workout.getName()+"', WTIME="+workout.getTime()+", PLACE='"+workout.getPlace()+"', DESCRIPTION='"+workout.getDescription()+"', CATEGORY='"+workout.getCategory()+"', WTYPE='"+workout.getType()+"', WDATE='"+workout.getDate()+"' WHERE ID="+workout.getId())) {

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
