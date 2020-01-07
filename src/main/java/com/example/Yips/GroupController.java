package com.example.Yips;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    Categories categories;



    @GetMapping("/newgroup")
    public String openNewGroup(Model model) {
        model.addAttribute("categories", categories.getCategories());
        model.addAttribute("group", new Group());
        return "newgroup";
    }

    @PostMapping("/newgroup")
    public String createNewGroup(@ModelAttribute Group group, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        Group addGroup = groupService.addGroup(group, user);
        System.out.println(group.getCategory());
        if(addGroup==null){
            return "newgroup";
        } else {
            return "group";
        }
    }



}
