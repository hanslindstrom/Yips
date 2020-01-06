package com.example.Yips;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User addUser(User user){
        User dbUser = userRepository.findByUsername(user.getUsername().toLowerCase());
        if(dbUser==null) {
            User addUser = new User(user.getUsername().toLowerCase(), user.getEmail(), encoder.encode(user.getPassword()), "USER");
            userRepository.saveUser(addUser);
            System.out.println("User added: "+user.getUsername());
            return user;
        } else {
            System.out.println("User already exist: "+dbUser.getUsername());
            return null;
        }
    }
}
