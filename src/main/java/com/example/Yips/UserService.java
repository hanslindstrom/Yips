package com.example.Yips;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private String addUserMessage = "";

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User addUser(User user){
        User dbUser = userRepository.findByUsername(user.getUsername().toLowerCase());
        if(dbUser==null) {
            User addUser = new User(user.getUsername().toLowerCase(), user.getEmail(), encoder.encode(user.getPassword()), "USER");
            userRepository.saveUser(addUser);
            this.addUserMessage = "Awesome! You can now log in with username: " + user.getUsername();
            return user;
        } else {
            this.addUserMessage =  "Oops! There is already a user with the username: " + dbUser.getUsername();
            return null;
        }
    }

    public String getAddUserMessage(){
        return addUserMessage;
    }
}
