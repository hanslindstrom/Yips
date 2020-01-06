package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class RController {

    @Autowired
    UserRepository userRepository;



    //--------USERS---------------------
    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User>users = userRepository.findAll();
        return users;
    }
    //--------USERS---------------------
}
