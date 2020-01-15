package com.example.Yips;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class WorkoutController {
    @Autowired
    WorkoutRepository workoutRepository;

    @Autowired
    ConnectionRepository connectionRepository;

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    Categories categories;

    @Autowired
    WorkoutService workoutService;

    @GetMapping("/workout")
    public String getWorkout(Model model, HttpSession session, Authentication authentication) {
        Long userId = userRepository.findByUsername(authentication.getName()).getId();
        Workout workout = (Workout)session.getAttribute("workout");
        model.addAttribute("workout", workout);
        model.addAttribute("exercise", new Exercise());
        List<String> priorExercises = exerciseRepository.findExerciseUserId(userId);
        model.addAttribute("priorexercises", priorExercises);


        //Add all exercises connected to workout to list to be able to print them in workout
        //1 get the workoutId from DB.
        Long databaseWorkoutId = workoutRepository.findByWorkoutname(workout.getName()).getId();

        //2 use the workoutId to get exercises.
        List<Exercise>exercises = connectionRepository.findExercisesInWorkoutByWorkoutId(databaseWorkoutId);

        //3 add list to model:
        model.addAttribute("exercises", exercises);

        // Send user groups to html to be able to share workout
        List<Group>groups = groupRepository.findAllMyGroups(userId);
        model.addAttribute("groups", groups);

        return "workout";
    }

    @GetMapping("/workout/{workoutId}")
    public String getWorkout(@PathVariable Long workoutId, Model model, HttpSession session, Authentication authentication) {
        Long userId = userRepository.findByUsername(authentication.getName()).getId();
//        Workout workout = (Workout)session.getAttribute("workout");
        Workout workout = workoutRepository.findByWorkoutId(workoutId);
        session.setAttribute("workout", workout);
        model.addAttribute("workout", workout);
        model.addAttribute("exercise", new Exercise());
        List<String> priorExercises = exerciseRepository.findExerciseUserId(userId);
        model.addAttribute("priorexercises", priorExercises);


        //Add all exercises connected to workout to list to be able to print them in workout
        //1 get the workoutId from DB.
        Long databaseWorkoutId = workoutRepository.findByWorkoutname(workout.getName()).getId();

        //2 use the workoutId to get exercises.
        List<Exercise>exercises = connectionRepository.findExercisesInWorkoutByWorkoutId(databaseWorkoutId);

        //3 add list to model:
        model.addAttribute("exercises", exercises);

        // Send user groups to html to be able to share workout
        List<Group>groups = groupRepository.findAllMyGroups(userId);
        model.addAttribute("groups", groups);

        return "workout";
    }
    @PostMapping("/postexercise")
    public String postExercise(@ModelAttribute Exercise exercise, Authentication authentication, HttpSession session) {
        System.out.println("HANS Inside postmapping postexer, before, ex. name: " + exercise.getName());
        System.out.println("Inside postmapping postexer, ex. id: " + exercise.getId());
        String oldWoName = "";
        //exercise.setId(exerciseRepository.findExerciseIdByExerciseName(exercise.getName()));

        System.out.println("AFTER: Inside postmapping postexer, after, ex. id: " + exercise.getId());

        Long userId = userRepository.findByUsername(authentication.getName()).getId();
        Workout workout = (Workout)session.getAttribute("workout");
        //if(exercise.getId() == null){
        System.out.println("NEW WO????   "+workout.getNewDoingDone());
            //if(workout.getNewDoingDone() == "NEW"){

            exerciseRepository.addExercise(exercise, userId);
            System.out.println("added exercise with name " + exercise.getName() + " with id " + exerciseRepository.findExerciseIdByExercise(exercise));
            Long workoutId = workout.getId();
            Long exerciseId = exerciseRepository.findExerciseIdByExercise(exercise);
            connectionRepository.workoutExerciseConnect(workoutId,exerciseId);
        //}else {
            //exerciseRepository.updateExercise(exercise);
            //System.out.println("ELSE Ex update" + exercise.getName());
        //}
//        Exercise origExerciseId = exerciseRepository.findExerciseById(exercise.getId());

        return "redirect:/workout";
    }

    @PostMapping("/postgroupsfromworkout")
    public String postGroupsFromWorkout(@RequestParam String sendGroups,HttpSession session, Authentication authentication) {
        Long userId = userRepository.findByUsername(authentication.getName()).getId();
        Workout workout = (Workout)session.getAttribute("workout");
        Long workoutId = workout.getId();
        workoutService.sendWorkoutToGroups(sendGroups, workoutId, userId);
        return "redirect:/workout";
    }

    @PostMapping("/doneworkout")
    public String doneWorkout(HttpSession session) {
        Workout workout = (Workout)session.getAttribute("workout");
        System.out.println(workout.getName());
        workout.setNewDoingDone("DONE");
        workoutRepository.updateWorkout(workout);

        return "redirect:/userstartpage";
    }














    /*@GetMapping("/newworkout")
    public String getNewWorkout(Authentication authentication, Model model) {
        Long workoutId = workoutRepository.initiateWorkout();
        Workout addWorkout = new Workout();
        addWorkout.setId(workoutId);
        model.addAttribute("workout", addWorkout);
        model.addAttribute("categories", categories.getCategories());
        connectionRepository.userWorkoutConnect(authentication.getName(), workoutId);
        return "newworkout";
    }

    @PostMapping("/newworkout")
    public String postWorkout(@ModelAttribute Workout workout) {
        workoutRepository.updateWorkout(workout);
        return "userStartPage";
    }*/

}
