package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public String showUserStartPage (Model model, Authentication authentication, HttpSession session) {
        //Initierar lite mål för att få skriva ut nåt.
        ArrayList<Goal> goalArrayList = new ArrayList<>();
        goalArrayList.add(new Goal("Vinterns utmaning", false));
        goalArrayList.add(new Goal("Sommarens utmaning", false));
        model.addAttribute("listOfGoalsForUser", goalArrayList);

        //WORKOUTS
        List<Workout> workouts = workoutRepository.workoutDateList();
        /*System.out.println("Not sorted arraylist");
        for(int i = 0; i < workouts.size(); i++)
            System.out.println(workouts.get(i).getDate());*/
        Collections.sort(workouts);
        /*System.out.println("Sorted arraylist");
        for(int i = 0; i < workouts.size(); i++) {
            System.out.println(workouts.get(i).getDate());
        }*/
        System.out.println("Most recent workout name: " + workouts.get(workouts.size()-1).getName() + " id: " + workouts.get(workouts.size()-1).getId());
        model.addAttribute("workout_mostRecent", workouts.get(workouts.size()-1));
        model.addAttribute("exerciseList_mostRecent", connectionRepository.findExercisesInWorkoutByWorkoutId(workouts.get(workouts.size()-1).getId()));

        System.out.println("Next workout: " + workouts.get(0).getName() + " id: " + workouts.get(0).getId());
        model.addAttribute("workout_next", workouts.get(0));
        model.addAttribute("exerciseList_workout_next", connectionRepository.findExercisesInWorkoutByWorkoutId(workouts.get(0).getId()));


        //Hämtar alla grupper för en person.
        Long userId = userRepository.findByUsername(authentication.getName()).getId();
        System.out.println("My userId is: " + userId);
        model.addAttribute("listofGroups", groupRepository.findAllMyGroups(userId));
        System.out.println("All my groups are: " + groupRepository.findAllMyGroups(userId));


        //Hämtar alla invites för en person.
        ArrayList<String> listOfGroupInvites = new ArrayList<>();
        ArrayList<String> listOfSenders = new ArrayList<>();

        List<String> groupSenderInvite = new ArrayList<>();
        List<List> inviteInfo = new ArrayList<>();


        String groupName;
        String senderName;
        Long inviteId;

        model.addAttribute("listofInvites", inviteRepository.getAllInvitesWithUserId(userId));
        for (Invite invite : inviteRepository.getAllInvitesWithUserId(userId)) {
            groupName = groupRepository.findGroupById(invite.getGroupid()).getName();
            senderName = userRepository.findByUserId(invite.getSenderid()).getUsername();
            inviteId = invite.getId();

            groupSenderInvite.add(groupName);
            groupSenderInvite.add(senderName);
            groupSenderInvite.add(inviteId.toString());

            inviteInfo.add(groupSenderInvite);

        }
        model.addAttribute("inviteInfo", inviteInfo);

        //Fixa med group modal.
        model.addAttribute("group", new Group());


        //Fixar med workout modal.
        Long workoutId = workoutRepository.initiateWorkout();
        Workout addWorkout = new Workout();
        addWorkout.setId(workoutId);
        model.addAttribute("workout", addWorkout);
        model.addAttribute("categories", categories.getCategories());
        connectionRepository.userWorkoutConnect(authentication.getName(), workoutId);


        return "userstartpage";
    }

    @PostMapping("/postworkout")
    public String postWorkout2(@ModelAttribute Workout workout, HttpSession session, Authentication authentication) {
        /*
        REMOVE WHEN WORKING
        Long workoutId = workoutRepository.initiateWorkout();
        Workout addWorkout = new Workout();
        addWorkout.setId(workoutId);
        connectionRepository.userWorkoutConnect(authentication.getName(), workoutId);
        */
        workout.setNewDoingDone("NEW");
        workoutRepository.updateWorkout(workout);
        session.setAttribute("workout", workout);
        return "redirect:/workout";
    }

    @PostMapping("/newgroup")
    public String createGroup (@ModelAttribute Group group, Authentication authentication, HttpSession session){
        group.setOwnerId(userRepository.findByUsername(authentication.getName()).getId());
        groupRepository.saveGroup(group);
        session.setAttribute("mygroup", group);
        return "redirect:/group";
    }


}
