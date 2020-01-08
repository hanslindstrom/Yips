package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
public class RController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    InviteRepository inviteRepository;



    //--------USERS---------------------
    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User>users = userRepository.findAll();
        return users;
    }
    //--------USERS---------------------

    //@CrossOrigin
    @GetMapping("/rest/getUser/{username}")
    public User getUser(@PathVariable String username, HttpSession session){
        User user = userRepository.findByUsername(username);
        Group group = (Group)session.getAttribute("mygroup");

        Long groupId = group.getId();
        Long recipientId = user.getId();
        Long senderId = group.getOwnerId();


        if (user == null){
            return new User();
        }
        inviteRepository.saveInvite(groupId, senderId, recipientId);
        return user;
    }
}
