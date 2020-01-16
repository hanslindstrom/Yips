package com.example.Yips;

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

    public List<Workout> findNewWorkoutsWithUserIdForJoel (Long userId) {
        List<Workout>newWorkouts = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM WORKOUT JOIN USERWORKOUTCONNECTION ON WORKOUT.ID=USERWORKOUTCONNECTION.WORKOUTID WHERE USERID=? AND NEWDOINGDONE = ?")){
            ps.setLong(1,userId);
            ps.setString(2, "NEW");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Workout workout=new Workout();
                workout.setId(rs.getLong("ID"));
                workout.setName(rs.getString("NAME"));
                if ( rs.getDate("WDATE") == null)
                    continue;
                workout.setDate(rs.getDate("WDATE").toLocalDate());
                workout.setType(rs.getString("WTYPE"));
                workout.setTime(rs.getInt("WTIME"));
                workout.setPlace(rs.getString("PLACE"));
                workout.setDescription(rs.getString("DESCRIPTION"));
                workout.setCategory(rs.getString("CATEGORY"));
                workout.setNewDoingDone(rs.getString("NEWDOINGDONE"));
                newWorkouts.add(workout);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return newWorkouts;
    }

    public List<Workout> findNewWorkoutsWithUserId (Long userId) {
        List<Workout>newWorkouts = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM WORKOUT JOIN USERWORKOUTCONNECTION ON WORKOUT.ID=USERWORKOUTCONNECTION.WORKOUTID WHERE USERID=?")){
            ps.setLong(1,userId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Workout workout=new Workout();
                workout.setId(rs.getLong("ID"));
                workout.setName(rs.getString("NAME"));
                if ( rs.getDate("WDATE") == null)
                    continue;
                workout.setDate(rs.getDate("WDATE").toLocalDate());
                workout.setType(rs.getString("WTYPE"));
                workout.setTime(rs.getInt("WTIME"));
                workout.setPlace(rs.getString("PLACE"));
                workout.setDescription(rs.getString("DESCRIPTION"));
                workout.setCategory(rs.getString("CATEGORY"));
                workout.setNewDoingDone(rs.getString("NEWDOINGDONE"));
                newWorkouts.add(workout);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return newWorkouts;
    }



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
    public List<Workout>workoutDateList (Long userId) {
        List<Workout> workoutDateList = new ArrayList<>();
        Workout workout = new Workout();
        try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM workout JOIN USERWORKOUTCONNECTION ON WORKOUT.ID=USERWORKOUTCONNECTION.WORKOUTID WHERE USERID=?")) {
            ps.setLong(1,userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                workout = new Workout();
                long id = rs.getInt("id");
                Date date = rs.getDate("WDATE");
                String name = rs.getString("NAME");
                String description = rs.getString("DESCRIPTION");
                String newDoingDone = rs.getString("NEWDOINGDONE");

                if(date == null)
                    continue;   //Sometimes empty workouts are created, and this becomes a null pointer exception
                workout.setId(id);
                workout.setName(name);
                LocalDate lDate=date.toLocalDate();
                workout.setDate(lDate);
                workout.setNewDoingDone(newDoingDone);
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

    public Workout findByWorkoutId(Long workoutId) {
        Workout workout = new Workout();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM workout WHERE id = ?")){
            ps.setString(1, workoutId.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                long id = rs.getInt("id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                String newdoingdone = rs.getString("NEWDOINGDONE");
                if( rs.getDate("WDATE") != null) {
                    LocalDate date = rs.getDate("WDATE").toLocalDate();
                    workout.setDate(date);
                }
                workout.setNewDoingDone(newdoingdone);
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
            PreparedStatement ps = conn.prepareStatement("UPDATE workout SET NAME='"+workout.getName()+"', WTIME='"+workout.getTime()+"', NEWDOINGDONE='"+workout.getNewDoingDone()+"', PLACE='"+workout.getPlace()+"', DESCRIPTION='"+workout.getDescription()+"', CATEGORY='"+workout.getCategory()+"', WTYPE='"+workout.getType()+"', WDATE='"+workout.getDate()+"' WHERE ID="+workout.getId())) {

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Add delete all workouts where name starts with workoutinit...

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

    public Long findWorkoutIdByWorkoutName (Workout workout) {
        Long workoutId=0L;
        List<Long>workoutIdList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT ID FROM workout WHERE NAME=?")){
            ps.setString(1,workout.getName());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Long workoutIdDb = rs.getLong("ID");
                workoutIdList.add(workoutIdDb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(Long id:workoutIdList) {
            if(id>workoutId) {
                workoutId=id;
            }
        }
        return workoutId;
    }


}
