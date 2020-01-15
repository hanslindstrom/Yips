package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        newWorkout.setNewDoingDone("Doing");
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
        int listLength = workoutRepository.findNewWorkoutsWithUserId(userId).size();
        List<Workout> workoutsInvite = workoutRepository.findNewWorkoutsWithUserId(userId);

        System.out.println("This is the listlength : " + listLength);

        model.addAttribute("invitesToWorkout", workoutsInvite);
        model.addAttribute("invitesToWorkoutLength", listLength);
    }





}
