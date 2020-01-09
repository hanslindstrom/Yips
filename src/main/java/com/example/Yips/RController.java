package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
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
        System.out.println("grupp id " + group.getId());
        if (user == null){
            return new User();
        }
        Long groupId = group.getId();
        Long recipientId = user.getId();
        Long senderId = group.getOwnerId();
        inviteRepository.saveInvite(groupId, senderId, recipientId);
        return user;
    }
    @GetMapping("/rest/getAllInvites")
    public List<Invite> getAllInvites(HttpSession session, Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName());
        System.out.println("THe user is " + user);

        List<Invite> inviteList = inviteRepository.getAllInvitesWithUserId(user.getId());
        return inviteList;
    }
}
