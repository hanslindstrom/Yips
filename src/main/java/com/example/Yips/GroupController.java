package com.example.Yips;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    Categories categories;



    @GetMapping("/newgroup")
    public String openNewGroup(Model model) {
        model.addAttribute("categories", categories.getCategories());
        model.addAttribute("group", new Group());
        return "newgroup";
    }

    @PostMapping("/newgroup")
    public String createNewGroup(@ModelAttribute Group group, HttpSession httpSession, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        Group addGroup = groupService.addGroup(group, user);
        System.out.println(group.getCategory());
        httpSession.setAttribute("mygroup", addGroup);
        if(addGroup==null){
            return "newgroup";
        } else {
            return "redirect:/group";
        }
    }

    @GetMapping("/group")
    public String seeMyGroup(HttpSession httpSession) {
        Group group = (Group)httpSession.getAttribute("mygroup");
       // Group group = (Group)model.getAttribute("group");
        System.out.println("GetMapping for /group, name: " + group.getName());
        httpSession.setAttribute("mygroup", groupRepository.findByGroupname(group.getName()));
        return "group";
    }



}
