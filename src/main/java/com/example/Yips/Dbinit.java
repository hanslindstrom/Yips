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

    public Dbinit (UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository=userRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {

        User dan = new User("dan", "dan@epost.com", encoder.encode("dan123"), "USER");
        User admin = new User("admin", "admin@epost.com", encoder.encode( "admin123"), "ADMIN");
        User hans = new User("hans", "hans@epost.com", encoder.encode("hans321"), "ADMIN");
        User duck = new User("donald", "donald@duck.se", encoder.encode("quack"), "USER");

        List<User>users = Arrays.asList(dan,admin,hans, duck);

        this.userRepository.saveAll(users);


    }

}
