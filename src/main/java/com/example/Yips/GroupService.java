package com.example.Yips;

import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group addGroup(Group group, User user){
        Group dbGroup = groupRepository.findByGroupname(group.getName().toLowerCase());
        if (dbGroup==null) {
            Group newGroup = new Group(group.getName().toLowerCase(), user.getId(), group.getCategory(), group.getDescription());
            groupRepository.saveGroup(newGroup);
            System.out.println("Created new group with the name: " + group.getName());
            return group;
        } else {
            System.out.println("You already have a group with the name: " + dbGroup.getName());
            return null;
        }

    }
}
