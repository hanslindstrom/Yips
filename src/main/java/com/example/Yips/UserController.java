package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    //--------NEW USER---------------------
    @PostMapping("/newuser")
    public String postUser(@ModelAttribute User user) {
        User addUser = userService.addUser(user);
        if(addUser==null) {
            return"/login";
        } else {
            return "redirect:/userStartPage";
        }
    }

    @GetMapping("/userStartPage")
    public String userStartPage() {
        return "userStartPage";
    }

    @GetMapping("/newuser")
    public String getNewUser(Model model) {
        model.addAttribute("user", new User());
        return "newuser";
    }
    //--------NEW USER---------------------
    //---------LOG IN----------------------
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/")
    public String start() {
        return ("/login");
    }
    //---------LOG IN----------------------
    //-----------USER----------------------
    @GetMapping("/user")
    public String user(){
        return "user";
    }
    //-----------USER----------------------
}
