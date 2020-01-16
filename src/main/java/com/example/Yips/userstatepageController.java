package com.example.Yips;

import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        //USER ID
        Long userId = userRepository.findByUsername(authentication.getName()).getId();


        //Initierar lite mål för att få skriva ut nåt.
        ArrayList<Goal> goalArrayList = new ArrayList<>();
        goalArrayList.add(new Goal("Vinterns utmaning", false));
        goalArrayList.add(new Goal("Sommarens utmaning", false));
        model.addAttribute("listOfGoalsForUser", goalArrayList);

        //INVITE TO WORKOUT
        int listLength = workoutRepository.findNewWorkoutsWithUserId(userId).size();
        List<Workout> workoutsInvite = workoutRepository.findNewWorkoutsWithUserId(userId);
        model.addAttribute("invitesToWorkout", workoutsInvite);
        model.addAttribute("invitesToWorkoutLength", listLength);


        //WORKOUTS
        List<Workout> workouts = workoutRepository.workoutDateList(userId);
        Collections.sort(workouts);
        List<Workout> doneWorkouts = new ArrayList<>();
        List<Workout> nextWorkouts = new ArrayList<>();
        for(Workout workout: workouts) {
            if(workout.getNewDoingDone()==null) {
                System.out.println("GOODBYE");
                System.out.println(workout.getName());
            }
            if (workout.getNewDoingDone().equalsIgnoreCase("DONE")) {
                doneWorkouts.add(workout);
            } else if (workout.getNewDoingDone().equalsIgnoreCase("DOING")) {
                nextWorkouts.add(workout);
            }
        }

        if(doneWorkouts.size()>0) {
            Workout workoutMostRecent = doneWorkouts.get(doneWorkouts.size() - 1);

            List<Long> groupIds = connectionRepository.findGroupsIdsConnectedToWorkoutByWorkoutId(workoutMostRecent.getId());

            List<Group> groupList = new ArrayList<>();
            if (groupIds.size() > 0) {
                System.out.println("test " + connectionRepository.findGroupsIdsConnectedToWorkoutByWorkoutId(workoutMostRecent.getId()).get(0));
                for (int i = 0; i < groupIds.size(); i++)
                    groupList.add(groupRepository.findGroupById(groupIds.get(i)));
                model.addAttribute("groupName_mostRecent_workout", groupList);
            } else
                model.addAttribute("groupName_mostRecent_workout", null);
            model.addAttribute("workout_mostRecent", workoutMostRecent);
            model.addAttribute("exerciseList_mostRecent", connectionRepository.findExercisesInWorkoutByWorkoutId(workoutMostRecent.getId()));
        }
        Workout workoutNext = new Workout();
        if(nextWorkouts.size()>0) {
            workoutNext = nextWorkouts.get(0);


        List<Long> groupIds2 = connectionRepository.findGroupsIdsConnectedToWorkoutByWorkoutId(workoutNext.getId());
        List<Group> groupList2 = new ArrayList<>();
        if(groupIds2.size() > 0) {
            for (int i = 0; i < groupIds2.size(); i++)
                groupList2.add(groupRepository.findGroupById(groupIds2.get(i)));
            model.addAttribute("groupName_mostRecent_workout", groupList2);
        }
        else
            model.addAttribute("groupName_next_workout",  null);
        model.addAttribute("workout_next", workoutNext);
        model.addAttribute("exerciseList_workout_next", connectionRepository.findExercisesInWorkoutByWorkoutId(workoutNext.getId()));

        }
        //Hämtar alla grupper för en person.
        System.out.println("My userId is: " + userId);
        model.addAttribute("listofGroups", groupRepository.findAllMyGroups(userId));
        System.out.println("All my groups are: " + groupRepository.findAllMyGroups(userId));


        //Hämtar alla invites för en person.
        List<Invite> inviteList= inviteRepository.getAllInvitesWithUserId(userId);
        if(inviteList == null) {
            model.addAttribute("groupInvitesLength", 0);
        }
        model.addAttribute("groupInvitesLength", inviteList.size());

        //En special lista skapas, med gruppnamn, inviteId och senderId. För att det är den datan vi vill displaya i userstartpage
        List<String> groupSenderInvite2 = new ArrayList<>();
        List<List> inviteInfo2 = new ArrayList<>();
        String groupName2;
        String senderName2;
        Long inviteId2;

        for (Invite invite : inviteList) {
            groupName2 = groupRepository.findGroupById(invite.getGroupid()).getName();
            senderName2 = userRepository.findByUserId(invite.getSenderid()).getUsername();
            inviteId2 = invite.getId();
            groupSenderInvite2.add(groupName2);
            groupSenderInvite2.add(senderName2);
            groupSenderInvite2.add(inviteId2.toString());

            inviteInfo2.add(groupSenderInvite2);
            System.out.println("groupname: " + groupName2 + " senderName " + senderName2 + "inviteId" + inviteId2);
        }
        model.addAttribute("inviteInfo2", inviteInfo2);

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
        System.out.println("Postmapping NEWGROUP group in param id: " + group.getId());
        group.setOwnerId(userRepository.findByUsername(authentication.getName()).getId());
        groupRepository.saveGroup(group);
        System.out.println("Group saved...");
        group = groupRepository.findByGroupname(group.getName()); //Kommer faila om flera grupper har samma namn
        System.out.println("SAMI get group by name - result id: " + group.getId());
        session.setAttribute("onegroup", group);
        System.out.println("added this group to session, id is: " + group.getId());
        System.out.println("----LEAVING POSTMAPPING redirect -----");
        return "redirect:/group";
    }





}
