package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@RestController
public class RController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    InviteRepository inviteRepository;

    @Autowired
    GroupRepository groupRepository;

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
    public List<List> getAllInvites(HttpSession session, Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName());
        ArrayList<String> listOfGroupInvites = new ArrayList<>();
        ArrayList<String> listOfSenders = new ArrayList<>();
        List<List> inviteInfoList = new ArrayList<>();


        for (Invite invite : inviteRepository.getAllInvitesWithUserId(user.getId())) {
            listOfGroupInvites.add(groupRepository.findGroupById(invite.getGroupid()).getName());
            listOfSenders.add(userRepository.findByUserId(invite.getSenderid()).getUsername());
        }

        inviteInfoList.add(inviteRepository.getAllInvitesWithUserId(user.getId()));
        inviteInfoList.add(listOfGroupInvites);
        inviteInfoList.add(listOfSenders);
        System.out.println(inviteInfoList);
        return inviteInfoList;
    }

    @DeleteMapping("/rest/declineInvite/{inviteId}")
    public void deleteInvite(@PathVariable Long inviteId) {
        inviteRepository.deleteInviteWithId(inviteId);
        System.out.println("Deleted invite with id " + inviteId);

    }

    @PutMapping("/rest/acceptInvite")
    public void acceptInvite(@RequestBody Invite invite) {
        System.out.println("requestbody: " + invite);
        //deleteInvite(invite.getId());
        System.out.println("Deleted invite with id " + invite.getId());


    }


}
