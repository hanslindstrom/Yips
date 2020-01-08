package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WorkoutController {
    @Autowired
    WorkoutRepository workoutRepository;

    @Autowired
    ConnectionRepository connectionRepository;

    @Autowired
    Categories categories;

    @GetMapping("/newworkout")
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
    }

}
