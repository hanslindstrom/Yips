package com.example.Yips;


import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        User hans = new User("hans", "hans@epost.com", encoder.encode("a"), "ADMIN");
        User johan = new User("johan", "johan@epost.com", encoder.encode("a"), "ADMIN");
        User sami = new User("sami", "sami@epost.com", encoder.encode("a"), "ADMIN");
        User joel = new User("joel", "joel@epost.com", encoder.encode("a"), "ADMIN");
        //User duck = new User("donald", "donald@duck.se", encoder.encode("quack"), "USER");

        List<User> users = Arrays.asList(dan,admin,hans, johan, sami, joel);

        this.userRepository.saveAll(users);

        //Add group

        Group groupOne = new Group("Team Yips", 5L, "Strength", "En fin grupp" );
        Group groupTwo = new Group("AJRONMÄN", 7L, "Cardio", "En uthållig grupp" );
        List<Group> groups = Arrays.asList(groupOne, groupTwo);
        this.groupRepository.saveAll(groups);
        groupOne = groupRepository.findByGroupname("Team Yips");
        groupTwo  = groupRepository.findByGroupname("AJRONMÄN");
        this.connectionRepository.connectNewMemberToGroup(groupOne, userRepository.findByUsername("hans").getId());
        //this.connectionRepository.connectNewMemberToGroup(groupOne, userRepository.findByUsername("johan").getId());
        this.connectionRepository.connectNewMemberToGroup(groupOne, userRepository.findByUsername("sami").getId());
        this.connectionRepository.connectNewMemberToGroup(groupOne, userRepository.findByUsername("joel").getId());
        //this.connectionRepository.connectNewMemberToGroup(groupTwo, userRepository.findByUsername("joel").getId());
        this.connectionRepository.connectNewMemberToGroup(groupTwo, userRepository.findByUsername("dan").getId());

        //Add some initial workouts...
        Workout workout = new Workout("Långlöp", "Löpning");
        this.workoutRepository.saveWorkout(workout, userRepository.findByUsername("admin"));
        Workout workoutDB = workoutRepository.findByWorkoutname(workout.getName());
        workoutDB.setDate(LocalDate.of(2020,1,1));
        workoutDB.setNewDoingDone("DONE");
        this.workoutRepository.updateWorkout(workoutDB);
        connectionRepository.userWorkoutConnect("admin", workoutDB.getId());

        Workout workoutYips = new Workout("Second coming", "Strength");
        this.workoutRepository.saveWorkout(workoutYips, userRepository.findByUsername("hans"));
        Workout workoutDBY = workoutRepository.findByWorkoutname(workoutYips.getName());
        workoutDBY.setDate(LocalDate.of(2020,1,17));
        workoutDBY.setNewDoingDone("NEW");
        workoutDBY.setType("AMRAP");
        workoutDBY.setTime(15);
        this.workoutRepository.updateWorkout(workoutDBY);
        connectionRepository.userWorkoutConnect("hans", workoutDBY.getId());
        connectionRepository.userWorkoutConnect("johan", workoutDBY.getId());
        connectionRepository.userWorkoutConnect("sami", workoutDBY.getId());
        connectionRepository.userWorkoutConnect("joel", workoutDBY.getId());

        Workout workoutYips2 = new Workout("TEAM OF 2", "Strength");
        this.workoutRepository.saveWorkout(workoutYips2, userRepository.findByUsername("hans"));
        Workout workoutDBY2 = workoutRepository.findByWorkoutname(workoutYips2.getName());
        workoutDBY2.setDate(LocalDate.of(2020,1,12));
        workoutDBY2.setNewDoingDone("DONE");
        workoutDBY2.setType("AMRAP");
        workoutDBY2.setTime(40);
        this.workoutRepository.updateWorkout(workoutDBY2);
        connectionRepository.userWorkoutConnect("hans", workoutDBY2.getId());
        connectionRepository.userWorkoutConnect("johan", workoutDBY2.getId());
        connectionRepository.userWorkoutConnect("sami", workoutDBY2.getId());
        connectionRepository.userWorkoutConnect("joel", workoutDBY2.getId());

        //Add exercise
        Exercise exerciseY1 = new Exercise();
        exerciseY1.setName("Squat Cleans");
        exerciseY1.setReps(10);
        exerciseY1.setWeight(70);
        this.exerciseRepository.addExercise(exerciseY1,4L);
        connectionRepository.workoutExerciseConnect(workoutDBY.getId(), exerciseRepository.findExerciseIdByExercise(exerciseY1));

        Exercise exerciseY2 = new Exercise();
        exerciseY2.setName("Handstand Pushups");
        exerciseY2.setReps(10);
        this.exerciseRepository.addExercise(exerciseY2,4L);
        connectionRepository.workoutExerciseConnect(workoutDBY.getId(), exerciseRepository.findExerciseIdByExercise(exerciseY2));

        Exercise exerciseY3 = new Exercise();
        exerciseY3.setName("Thrusters");
        exerciseY3.setReps(24);
        this.exerciseRepository.addExercise(exerciseY3,4L);
        connectionRepository.workoutExerciseConnect(workoutDBY2.getId(), exerciseRepository.findExerciseIdByExercise(exerciseY3));

        Exercise exerciseY4 = new Exercise();
        exerciseY4.setName("Box jumps");
        exerciseY4.setReps(24);
        this.exerciseRepository.addExercise(exerciseY4,4L);
        connectionRepository.workoutExerciseConnect(workoutDBY2.getId(), exerciseRepository.findExerciseIdByExercise(exerciseY4));

        Exercise exerciseY5 = new Exercise();
        exerciseY5.setName("Pull-ups");
        exerciseY5.setReps(24);
        this.exerciseRepository.addExercise(exerciseY5,4L);
        connectionRepository.workoutExerciseConnect(workoutDBY2.getId(), exerciseRepository.findExerciseIdByExercise(exerciseY5));

        Exercise exerciseY6 = new Exercise();
        exerciseY6.setName("BF Burpees");
        exerciseY6.setReps(24);
        this.exerciseRepository.addExercise(exerciseY6,4L);
        connectionRepository.workoutExerciseConnect(workoutDBY2.getId(), exerciseRepository.findExerciseIdByExercise(exerciseY6));

        Exercise exerciseY7 = new Exercise();
        exerciseY7.setName("Shoulder to overhead");
        exerciseY7.setReps(24);
        this.exerciseRepository.addExercise(exerciseY7,4L);
        connectionRepository.workoutExerciseConnect(workoutDBY2.getId(), exerciseRepository.findExerciseIdByExercise(exerciseY7));

        Exercise exerciseY8 = new Exercise();
        exerciseY8.setName("Power Snatch");
        exerciseY8.setReps(24);
        this.exerciseRepository.addExercise(exerciseY8,4L);
        connectionRepository.workoutExerciseConnect(workoutDBY2.getId(), exerciseRepository.findExerciseIdByExercise(exerciseY8));

        Exercise exerciseY9 = new Exercise();
        exerciseY9.setName("HSPU");
        exerciseY9.setReps(24);
        this.exerciseRepository.addExercise(exerciseY9,4L);
        connectionRepository.workoutExerciseConnect(workoutDBY2.getId(), exerciseRepository.findExerciseIdByExercise(exerciseY9));

        Exercise exerciseY10 = new Exercise();
        exerciseY10.setName("Double Unders");
        exerciseY10.setReps(24);
        this.exerciseRepository.addExercise(exerciseY10,4L);
        connectionRepository.workoutExerciseConnect(workoutDBY2.getId(), exerciseRepository.findExerciseIdByExercise(exerciseY10));


        Workout workout2 = new Workout("Långsim", "Simning");
        this.workoutRepository.saveWorkout(workout2, userRepository.findByUsername("admin"));
        Workout workoutDB2 = workoutRepository.findByWorkoutname(workout2.getName());
        workoutDB2.setDate(LocalDate.of(2020,7,10));
        workoutDB2.setNewDoingDone("NEW");
        this.workoutRepository.updateWorkout(workoutDB2);
        connectionRepository.userWorkoutConnect("admin", workoutDB2.getId());

        Workout workout3 = new Workout("Kortlöp", "Löpning");
        this.workoutRepository.saveWorkout(workout3, userRepository.findByUsername("admin"));
        Workout workoutDB3 = workoutRepository.findByWorkoutname(workout3.getName());
        workoutDB3.setDate(LocalDate.of(2020,1,10));
        workoutDB3.setNewDoingDone("NEW");
        this.workoutRepository.updateWorkout(workoutDB3);
        connectionRepository.userWorkoutConnect("admin", workoutDB3.getId());

        //Add exercise
        Exercise exercise = new Exercise();
        exercise.setName("squat");
        exercise.setSets(5);
        exercise.setReps(5);
        this.exerciseRepository.addExercise(exercise,4L);
        connectionRepository.workoutExerciseConnect(workoutDB3.getId(), exerciseRepository.findExerciseIdByExercise(exercise));

        exercise = new Exercise();
        exercise.setName("double unders");
        exercise.setSeconds(120);
        exercise.setReps(100);
        this.exerciseRepository.addExercise(exercise,4L);
        connectionRepository.workoutExerciseConnect(workoutDB3.getId(), exerciseRepository.findExerciseIdByExercise(exercise));



    }

}
