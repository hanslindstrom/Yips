package com.example.Yips;

import org.hibernate.jdbc.Work;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
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

    public Dbinit (UserRepository userRepository, PasswordEncoder encoder, GroupRepository groupRepository, WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository) {
        this.userRepository=userRepository;
        this.encoder = encoder;
        this.groupRepository = groupRepository;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
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

        //Add workout
        Workout workout = new Workout("Långlöp", "Löpning");
        Workout workout1 = new Workout("Kortlöp", "Löpning");
        Workout workout2 = new Workout("Simmning superlångt", "Simning");
        //List <Workout> workouts = Arrays.asList(workout);
        this.workoutRepository.saveWorkout(workout, userRepository.findByUsername("dan"));
        Workout workoutDb=workoutRepository.findByWorkoutname(workout.getName());
        workoutDb.setDate(LocalDate.of(2020,6,30));
        workoutRepository.updateWorkout(workoutDb);

        this.workoutRepository.saveWorkout(workout1, userRepository.findByUsername("dan"));
        Workout workoutDb2=workoutRepository.findByWorkoutname(workout1.getName());
        workoutDb2.setDate(LocalDate.of(2020,1,30));
        workoutRepository.updateWorkout(workoutDb2);

        this.workoutRepository.saveWorkout(workout2, userRepository.findByUsername("dan"));
        Workout workoutDb3=workoutRepository.findByWorkoutname(workout2.getName());
        workoutDb3.setDate(LocalDate.of(2020,1,5));
        workoutRepository.updateWorkout(workoutDb3);

        //Add exercise
        Exercise exercise = new Exercise();
        exercise.setName("squat");
        exercise.setSets(5);
        exercise.setReps(5);
        this.exerciseRepository.addExercise(exercise,4L);

        exercise = new Exercise();
        exercise.setName("double unders");
        exercise.setSeconds(120);
        exercise.setReps(100);
        this.exerciseRepository.addExercise(exercise,4L);


    }

}
