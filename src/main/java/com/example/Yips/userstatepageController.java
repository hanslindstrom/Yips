package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class userstatepageController {

    @Autowired
    WorkoutRepository workoutRepository;

    @Autowired
    Categories categories;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    ConnectionRepository connectionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InviteRepository inviteRepository;

    @GetMapping ("/userstartpage")
    public String showUserStartPage (Model model, Authentication authentication) {
        //Initierar lite mål för att få skriva ut nåt.
        ArrayList<Goal> goalArrayList = new ArrayList<>();
        goalArrayList.add(new Goal("Vinterns utmaning", false));
        goalArrayList.add(new Goal("Sommarens utmaning", false));

        //Hämtar alla grupper för en person.
        Long userId = userRepository.findByUsername(authentication.getName()).getId();
        System.out.println("My userId is: " + userId);
        model.addAttribute("listofGroups", groupRepository.findAllMyGroups(userId));
        System.out.println("All my groups are: " + groupRepository.findAllMyGroups(userId));

        //Hämtar alla invites för en person.
        ArrayList<String> listOfGroupInvites = new ArrayList<>();
        ArrayList<String> listOfSenders = new ArrayList<>();

        model.addAttribute("listofInvites", inviteRepository.getAllInvitesWithUserId(userId));
        for (Invite invite : inviteRepository.getAllInvitesWithUserId(userId)) {
            listOfGroupInvites.add(groupRepository.findGroupById(invite.getGroupid()).getName());
            listOfSenders.add(userRepository.findByUserId(invite.getSenderid()).getUsername());
        }
        model.addAttribute("listOfGroupInvites", listOfGroupInvites);
        model.addAttribute("listOfSenders", listOfSenders);

        //Fixar med workout.
        Long workoutId = workoutRepository.initiateWorkout();
        Workout addWorkout = new Workout();
        addWorkout.setId(workoutId);
        model.addAttribute("workout", addWorkout);
        model.addAttribute("categories", categories.getCategories());
        connectionRepository.userWorkoutConnect(authentication.getName(), workoutId);


        model.addAttribute("listOfGoalsForUser", goalArrayList);
        return "userstartpage";
    }

    @PostMapping("/test")
    public String postWorkout2(@ModelAttribute Workout workout) {
        workoutRepository.updateWorkout(workout);
        return "userStartPage";
    }


}
