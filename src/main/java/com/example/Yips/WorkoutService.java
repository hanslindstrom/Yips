package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
public class WorkoutService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    ConnectionRepository connectionRepository;

    public void sendWorkoutToGroups(String sendGroups, Long workoutId, Long userId) {
        String[]groupNameArray = sendGroups.split(",");
        for(String groupName:groupNameArray) {
            Group group = groupRepository.findByGroupname(groupName);
            connectionRepository.groupWorkoutConnect(group.getId(), workoutId);
            //Find users connected to group
            List<User>usersInGroup = groupRepository.getAllMembers(group);

            //Connect users to workout

            for(User user:usersInGroup){

                if(user.getId()!=userId) {
                    System.out.println("user " + user.getId()+" is now connected to workout " +workoutId);
                    connectionRepository.userWorkoutConnect(user.getUsername(), workoutId);
                } else {
                    System.out.println("user " + user.getId()+" allready connected to workout "+workoutId);
                }

            }
        }
    }

}
