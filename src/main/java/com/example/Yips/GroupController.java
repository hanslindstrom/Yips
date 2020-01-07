package com.example.Yips;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GroupController {
/*

    @Autowired
    GroupService groupService;
*/


    @GetMapping("/newgroup")
    public String openNewGroup() {
        return "newgroup";
    }/*

    @PostMapping("/newgroup")
    public String createNewGroup(@ModelAttribute User user, @ModelAttribute Group group) {
        Group addGroup = groupService.addGroup(group, user);
        if(addGroup==null){
            return "newgroup";
        } else {
            return "newgroup";
        }
    }
*/


}
