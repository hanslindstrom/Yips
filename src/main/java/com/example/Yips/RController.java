package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RestController
public class RController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    InviteRepository inviteRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    WorkoutRepository workoutRepository;

    @Autowired
    ConnectionRepository connectionRepository;

    @Autowired
    ExerciseRepository exerciseRepository;

    //--------USERS---------------------
    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User>users = userRepository.findAll();
        return users;
    }
    //--------USERS---------------------

    //@CrossOrigin

    @GetMapping ("/rest/declineWorkoutInvite/{workoutId}")
    public void declineWorkoutInvite (@PathVariable long workoutId, Authentication authentication) {
        Long oldWorkoutId = workoutId;
        Workout newWorkout = workoutRepository.findByWorkoutId(workoutId);
        List<Exercise> newExerciselist = connectionRepository.findExercisesInWorkoutByWorkoutId(newWorkout.getId());
        User currentUser = userRepository.findByUsername(authentication.getName());

        for (Exercise newExercise : newExerciselist) {
            Long oldExerciseId=newExercise.getId();
            connectionRepository.deleteUserExerciseConnection(currentUser.getId(),oldExerciseId);
        }
        connectionRepository.deleteUserWorkoutConnection(currentUser.getId(), oldWorkoutId);

    }

    @GetMapping ("/rest/acceptWorkoutInvite/{workoutId}")
    public void acceptWorkoutInvite (@PathVariable long workoutId, Authentication authentication) {
        System.out.println("AAA Workout id to accept " + workoutId);
        Long oldWorkoutId = workoutId;
        Workout newWorkout = workoutRepository.findByWorkoutId(workoutId);
        newWorkout.setNewDoingDone("DOING");
        System.out.println("Date in newWorkout " + newWorkout.getDate());
        List<Exercise> newExerciselist = connectionRepository.findExercisesInWorkoutByWorkoutId(newWorkout.getId());
        User currentUser = userRepository.findByUsername(authentication.getName());

        //Duplicating workout.
        workoutRepository.saveWorkout(newWorkout, currentUser);
        Long newWorkoutId = workoutRepository.findWorkoutIdByWorkoutName(newWorkout);
        newWorkout.setId(newWorkoutId);
        workoutRepository.updateWorkout(newWorkout);
        connectionRepository.userWorkoutConnect(currentUser.getUsername(), newWorkoutId);

        //REMOVING ORIG EXERCISES FROM USEREXERCISESCONNECTION


        for (Exercise newExercise : newExerciselist) {
            exerciseRepository.addExercise(newExercise, currentUser.getId());
            Long oldExerciseId=newExercise.getId();
            //Delete old connections
            connectionRepository.deleteUserExerciseConnection(currentUser.getId(),oldExerciseId);

            newExercise.setId(exerciseRepository.findExerciseIdByExercise(newExercise));
            connectionRepository.workoutExerciseConnect(newWorkout.getId(), newExercise.getId());
            //user exercise connection happens when adding exercise, no need to connect.
        }
        connectionRepository.deleteUserWorkoutConnection(currentUser.getId(), oldWorkoutId);
//        //FINDING ORIG GROUPID - FULA VÄGEN
//        List<Long> groupListId = connectionRepository.findGroupsIdsConnectedToWorkoutByWorkoutId(workoutId);
//        for(Long groupId : groupListId) {
//            connectionRepository.connectNewWorkoutOrigWorkout(workoutId, newWorkoutId, groupId);
//        }




    }

    @GetMapping("/rest/getUser/{username}")
    public User getUser(@PathVariable String username, HttpSession session){
        User user = userRepository.findByUsername(username);
        Group group = (Group)session.getAttribute("onegroup");
        if (user == null){
            return new User();
        }
        Long groupId = group.getId();
        Long recipientId = user.getId();
        Long senderId = group.getOwnerId();
        inviteRepository.saveInvite(groupId, senderId, recipientId);
        return user;
    }

    @GetMapping ("/rest/acceptInviteGroup/{inviteId}")
    public void acceptGroupInvite (@PathVariable long inviteId, Authentication authentication) {
        Invite invite = inviteRepository.getInviteWithId(inviteId);
        User user = userRepository.findByUsername(authentication.getName());
        Group group = groupRepository.findGroupById(invite.getGroupid());
        connectionRepository.connectNewMemberToGroup(group, user.getId());
        inviteRepository.deleteInviteWithId(inviteId);
    }

    @GetMapping("/rest/declineInviteGroup/{inviteId}")
    public void  declineGroupInvite (@PathVariable long inviteId) {
        inviteRepository.deleteInviteWithId(inviteId);
    }


    @GetMapping("/rest/getAllInvites")
    public List<List> getAllInvites(Authentication authentication){
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
        System.out.println("this is all the invites we return to JS" + inviteInfoList.get(0));
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
    @GetMapping ("/rest/acceptInvite/{inviteId}")
    public Group acceptInvite (@PathVariable Long inviteId){
        System.out.println(inviteId);
        Invite invite = inviteRepository.getInviteWithId(inviteId);
        inviteRepository.deleteInviteWithId(inviteId);
        return groupRepository.findGroupById(invite.getGroupid());
    }
    @GetMapping ("/rest/getAllGroups")
    public List<Group> getAllGroupsForUser (Authentication authentication){
        return groupRepository.findAllMyGroups(userRepository.findByUsername(authentication.getName()).getId());
    }

    // TODO edit exercise and update old one
    @GetMapping("/rest/editExercise/{exerciseName}")
    public Exercise editExercise (@PathVariable String exerciseName, Model model, @ModelAttribute Exercise exercise) {
        System.out.println("We get " + exerciseName);
        Long exerciseIdToUpdate = exerciseRepository.findExerciseIdByExerciseName(exerciseName);
        Exercise exerciseToUpdate = exerciseRepository.findExerciseById(exerciseIdToUpdate);
        model.addAttribute("exercise", exerciseToUpdate);
        System.out.println("added exercise to update to the model, id: " + exerciseToUpdate.getId());
        return exerciseToUpdate;
    }
    // SAMI: delete @ModelAttribute


//
//    @PostMapping ("/rest/editExercise/{exerciseId}")
//        public Exercise updateExercise (@PathVariable Long exerciseId,Model model, ModelAttribute Exercise exercise){
//
//        }


    @GetMapping ("/rest/getAllGroupMembers/{groupid}")
    public List<User> getAllGroupsMembers (@PathVariable Long groupid){
        for(User user : groupRepository.getAllMembersWithGroupId(groupid))
            System.out.println("user in group " + user.getUsername());
        return groupRepository.getAllMembersWithGroupId(groupid);
    }

    @GetMapping ("/rest/updateWorkoutInvite")
    public void updateWorkoutInvite (Model model, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        long userId = user.getId();
        List<Workout> workoutsInvite = workoutRepository.findNewWorkoutsWithUserIdForJoel(userId);

        int listLength = workoutsInvite.size();
        System.out.println("we are now updating workoutinivte list, which now is " + listLength);
        System.out.println("This is the listlength : " + listLength);

        model.addAttribute("invitesToWorkout", workoutsInvite);
        model.addAttribute("invitesToWorkoutLength", listLength);
    }

    @GetMapping ("/rest/getNextWorkouts")
    public Workout getNextWorkouts (Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        List<Long> workoutIds = connectionRepository.findAllWorkoutIdsConnectedToUserWithUserId(user.getId());
        List<Workout> workouts = new ArrayList<>();
        for (Long workoutId : workoutIds) {
            if(workoutRepository.findByWorkoutId(workoutId).getDate() == null) {
                continue;
            }
            else
            workouts.add(workoutRepository.findByWorkoutId(workoutId));
        }
        Collections.sort(workouts);
        System.out.println("this is the size of workoutIds: " + workoutIds.size());
        System.out.println("this is the size of workouts: " + workouts.size());
        List<Workout> doneWorkouts = new ArrayList<>();
        List<Workout> nextWorkouts = new ArrayList<>();
        for(Workout workout: workouts) {
            if(workout.getNewDoingDone() == null) {
                System.out.println("GOODBYE");
                System.out.println("This is the newdoingdone" + workout.getNewDoingDone());
                System.out.println(workout.getName());
                continue;
            }
            if (workout.getNewDoingDone().equalsIgnoreCase("DONE")) {
                doneWorkouts.add(workout);
            } else if (workout.getNewDoingDone().equalsIgnoreCase("DOING")) {
                nextWorkouts.add(workout);
                System.out.println("We found a doing exercise!");
            }
        }
        if(nextWorkouts.size() == 0) {
            System.out.println("There is no new workouts ");
            return new Workout();
        }
            else
            System.out.println("next workout is.... " + nextWorkouts.get(nextWorkouts.size()-1).getName());
        return nextWorkouts.get(nextWorkouts.size()-1);
    }

    @GetMapping ("/rest/getExerciseListNextWorkout/{workoutId}")
    public List<Exercise> getExerciseListNextWorkout (@PathVariable Long workoutId) {
        System.out.println("THis is the new workout to get exercise list " + workoutId);
        List<Exercise> exercises = connectionRepository.findExercisesInWorkoutByWorkoutId(workoutId);
        return exercises;
    }

    @GetMapping("/rest/getMostRecentWorkout")
    public Workout getMostRecentWorkout (Authentication authentication) {
        //GETS ALL WORKOUTS CONNECTED TO USERS AND SORTS ON DATE
        User user = userRepository.findByUsername(authentication.getName());
        List<Long> workoutIds = connectionRepository.findAllWorkoutIdsConnectedToUserWithUserId(user.getId());
        List<Workout> workouts = new ArrayList<>();
        for (Long workoutId : workoutIds) {
            if(workoutRepository.findByWorkoutId(workoutId).getDate() == null) {
                continue;
            }
            else
                workouts.add(workoutRepository.findByWorkoutId(workoutId));
        }
        Collections.sort(workouts);

        //SORTS WORKOUTS ON DOING OR NEW WORKOUT
        List<Workout> doneWorkouts = new ArrayList<>();
        for(Workout workout: workouts) {
            if(workout.getNewDoingDone() == null) {
                System.out.println("GOODBYE");
                System.out.println("This is the newdoingdone" + workout.getNewDoingDone());
                System.out.println(workout.getName());
                continue;
            }
            if (workout.getNewDoingDone().equalsIgnoreCase("DONE")) {
                doneWorkouts.add(workout);
                System.out.println("We found a done exercise!");

            }
        }
        if(doneWorkouts.size() == 0) {
            System.out.println("There is no done workouts ");
            return new Workout();
        }
        else
            System.out.println("next workout is.... " + doneWorkouts.get(doneWorkouts.size()-1).getName());
        return doneWorkouts.get(doneWorkouts.size()-1);

    }

    @GetMapping ("/rest/getExerciseListMostRecentWorkout/{workoutId}")
    public List<Exercise> getExerciseListMostRecentWorkout (@PathVariable Long workoutId) {
        System.out.println("THis is the most recent workout to get exercise list " + workoutId);
        List<Exercise> exercises = connectionRepository.findExercisesInWorkoutByWorkoutId(workoutId);
        return exercises;
    }





}
