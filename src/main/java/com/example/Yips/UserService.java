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
            addUserMessage = "Awesome! You can now log in with username: " + user.getUsername();
            setAddUserMessage(addUserMessage);
            System.out.println(addUserMessage);
            return user;
        } else {
            addUserMessage = "Oops! There is already a user with the username: " + dbUser.getUsername();
            setAddUserMessage(addUserMessage);
            System.out.println(addUserMessage);
            return null;
        }
    }

    public void setAddUserMessage(String result){
        this.addUserMessage = result;
    }

    public String getAddUserMessage(){
        return addUserMessage;
    }
}
