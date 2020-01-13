package com.example.Yips;


import org.hibernate.jdbc.Work;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//CommandLineRunner makes this run as the application starts.
//This class is only used to add initial values to database.
@Service
public class Dbinit implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private GroupRepository groupRepository;
    private WorkoutRepository workoutRepository;
    private ExerciseRepository exerciseRepository;
    private ConnectionRepository connectionRepository;

    public Dbinit (UserRepository userRepository, PasswordEncoder encoder, GroupRepository groupRepository, WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository, ConnectionRepository connectionRepository) {
        this.userRepository=userRepository;
        this.encoder = encoder;
        this.groupRepository = groupRepository;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.connectionRepository = connectionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //Add users
        User dan = new User("dan", "dan@epost.com", encoder.encode("dan123"), "USER");
        User admin = new User("admin", "admin@epost.com", encoder.encode( "admin123"), "ADMIN");
        User hans = new User("a", "a@epost.com", encoder.encode("a"), "ADMIN");
        User duck = new User("donald", "donald@duck.se", encoder.encode("quack"), "USER");

        List<User> users = Arrays.asList(dan,admin,hans, duck);

        this.userRepository.saveAll(users);

        //Add group

        Group groupOne = new Group("our group", 3L, "löpning", "en testgrupp" );
        List<Group> groups = Arrays.asList(groupOne);
        this.groupRepository.saveAll(groups);
        groupOne = groupRepository.findByGroupname("our group");
        this.connectionRepository.connectNewMemberToGroup(groupOne,userRepository.findByUsername("dan").getId());

        //Add some initial workouts...
        Workout workout = new Workout("Långlöp", "Löpning");
        this.workoutRepository.saveWorkout(workout, userRepository.findByUsername("dan"));
        Workout workoutDB = workoutRepository.findByWorkoutname(workout.getName());
        workoutDB.setDate(LocalDate.of(2020,6,15));
        this.workoutRepository.updateWorkout(workoutDB);

        Workout workout2 = new Workout("Långsim", "Simning");
        this.workoutRepository.saveWorkout(workout2, userRepository.findByUsername("dan"));
        Workout workoutDB2 = workoutRepository.findByWorkoutname(workout2.getName());
        workoutDB2.setDate(LocalDate.of(2020,7,10));
        this.workoutRepository.updateWorkout(workoutDB2);

        Workout workout3 = new Workout("Kortlöp", "Löpning");
        this.workoutRepository.saveWorkout(workout3, userRepository.findByUsername("dan"));
        Workout workoutDB3 = workoutRepository.findByWorkoutname(workout3.getName());
        workoutDB3.setDate(LocalDate.of(2020,1,10));
        this.workoutRepository.updateWorkout(workoutDB3);

        //Add exercise
        Exercise exercise = new Exercise();
        exercise.setName("squat");
        exercise.setSets(5);
        exercise.setReps(5);
        this.exerciseRepository.addExercise(exercise,4L);
        connectionRepository.workoutExerciseConnect(workoutDB3.getId(), exerciseRepository.findExerciseIdByExerciseName(exercise));

        exercise = new Exercise();
        exercise.setName("double unders");
        exercise.setSeconds(120);
        exercise.setReps(100);
        this.exerciseRepository.addExercise(exercise,4L);
        connectionRepository.workoutExerciseConnect(workoutDB3.getId(), exerciseRepository.findExerciseIdByExerciseName(exercise));



    }

}
