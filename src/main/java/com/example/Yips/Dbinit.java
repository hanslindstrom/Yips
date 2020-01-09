package com.example.Yips;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Dbinit (UserRepository userRepository, PasswordEncoder encoder, GroupRepository groupRepository, WorkoutRepository workoutRepository) {
        this.userRepository=userRepository;
        this.encoder = encoder;
        this.groupRepository = groupRepository;
        this.workoutRepository = workoutRepository;
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
        //List <Workout> workouts = Arrays.asList(workout);
        this.workoutRepository.saveWorkout(workout, userRepository.findByUsername("dan"));



    }

}
