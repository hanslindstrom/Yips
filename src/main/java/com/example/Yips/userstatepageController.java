package com.example.Yips;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class userstatepageController {

    @GetMapping ("/userstartpage")
    public String showUserStartPage (Model model) {
        //Initierar lite mål för att få skriva ut nåt.
        ArrayList<Goal> goalArrayList = new ArrayList<>();
        goalArrayList.add(new Goal("Vinterns utmaning", false));
        goalArrayList.add(new Goal("Sommarens utmaning", false));


        model.addAttribute("listOfGoalsForUser", goalArrayList);
        return "userstartpage";
    }


}
