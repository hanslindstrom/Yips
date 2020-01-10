package com.example.Yips;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.security.PublicKey;
import java.util.List;

@Controller
public class GroupController {

    @Autowired
    GroupService groupService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    GoalRepository goalRepository;
    @Autowired
    InviteRepository inviteRepository;
    @Autowired
    Categories categories;



    @GetMapping("/newgroup")
    public String openNewGroup(Model model) {
        model.addAttribute("categories", categories.getCategories());
        model.addAttribute("group", new Group());
        return "newgroup";
    }

    /*@PostMapping("/newgroup2")
    public String createNewGroup(@ModelAttribute Group group, HttpSession session, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        Group addGroup = groupService.addGroup(group, user);
        System.out.println(group.getCategory());
        session.setAttribute("mygroup", addGroup);
        if(addGroup==null){
            return "newgroup";
        } else {
            return "redirect:/group";
        }
    }*/



    @GetMapping("/group")
    public String seeMyGroup(HttpSession session) {
        Group group = (Group)session.getAttribute("mygroup");
        session.setAttribute("listOfMembers", groupRepository.getAllMembers(group));
        System.out.println("GetMapping for /group, name: " + group.getName());
        session.setAttribute("mygroup", groupRepository.findByGroupname(group.getName()));
        //session.setAttribute("ourgoal", goalRepository.getGoalByGroupId(group));
        //System.out.println("Goal: " + goalRepository.getGoalByGroupId(group));
        return "group";
    }


}
