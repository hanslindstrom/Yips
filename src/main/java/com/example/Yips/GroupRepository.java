package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GroupRepository {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    UserRepository userRepository;

    public List<Group> findAll() {
        List<Group> groups = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM wagroup")){
            while(rs.next()) {
                long id = rs.getInt("id");
                String name = rs.getString("groupname");
                long ownerId = rs.getInt("ownerid");
                String category = rs.getString("category");
                String description = rs.getString("description");

                Group group = new Group();
                group.setId(id);
                group.setName(name);
                group.setOwnerId(ownerId);
                group.setCategory(category);
                group.setDescription(description);
                groups.add(group);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }

    public Group findByGroupname(String groupname) {
        Group group = new Group();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM wagroup WHERE groupname = ?")){
            ps.setString(1, groupname);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                long id = rs.getInt("id");
                String name = rs.getString("groupname");
                long ownerId = rs.getInt("ownerid");
                String category = rs.getString("category");
                String description = rs.getString("description");
                group.setId(id);
                group.setName(name);
                group.setOwnerId(ownerId);
                group.setCategory(category);
                group.setDescription(description);

            } else {
                group=null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    public void saveGroup(Group group) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO wagroup(GROUPNAME, OWNERID, CATEGORY, DESCRIPTION) VALUES(?,?,?,?)")){
            ps.setString(1,group.getName());
            ps.setLong(2, group.getOwnerId());
            ps.setString(3, group.getCategory());
            ps.setString(4, group.getDescription());
            ps.executeUpdate();

            Long id = findByGroupname(group.getName()).getId();
            connectionRepository.saveUserGroupConnection(group, id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //To add testgroups in DBinit....
    public void saveAll(List<Group> groups) {
        for(Group group:groups) {
            saveGroup(group);
        }
    }

    public List<User> getAllMembers(Group group){
        Group dbGroup = findByGroupname(group.getName().toLowerCase());
        List<User> members = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT userid FROM usergroupconnection WHERE groupid = ?")){
            ps.setString(1, dbGroup.getId().toString());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                long id = rs.getInt("userid");
                System.out.println("Member id: " + id);
                members.add(userRepository.findByUserId(id));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("All members: " + members);
        return members;

    }

    public List<Group> findAllMyGroups(Long userId) {
        List<Group> groups = new ArrayList<>();
        List<Long> groupIds = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM USERGROUPCONNECTION WHERE USERID = ? ")) {
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                long groupId = rs.getInt("groupid");
                groupIds.add(groupId);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        for (Long groupId : groupIds) {
            groups.add(findGroupById(groupId));
        }
        return groups;
    }

    public Group findGroupById(Long groupId) {
        Group group = new Group();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM wagroup WHERE id = ?")){
            ps.setString(1, groupId.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                long id = rs.getInt("id");
                String name = rs.getString("groupname");
                long ownerId = rs.getInt("ownerid");
                String category = rs.getString("category");
                String description = rs.getString("description");
                group.setId(id);
                group.setName(name);
                group.setOwnerId(ownerId);
                group.setCategory(category);
                group.setDescription(description);

            } else {
                group=null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

}
