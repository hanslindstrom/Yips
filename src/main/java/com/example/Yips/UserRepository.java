package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class UserRepository {

    @Autowired
    private DataSource dataSource;

    public List<User>findAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM wauser")){
            while(rs.next()) {
                long id = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");

                User user = new User();
                user.setId(id);
                user.setUsername(username);
                user.setEmail(email);
                users.add(user);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public User findByUsername(String username) {
        User user = new User();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM wauser WHERE username = ?")){
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Long id = rs.getLong("id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String roles = rs.getString("roles");
                int active = rs.getInt("active");
                user.setId(id);
                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(password);
                user.setRoles(roles);
                user.setActive(active);

            } else {
                user=null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User findByUserId(Long userId) {
        User user = new User();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM wauser WHERE id = ?")){
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String roles = rs.getString("roles");
                int active = rs.getInt("active");
                user.setId(id);
                user.setUsername(name);
                user.setEmail(email);
                user.setPassword(password);
                user.setRoles(roles);
                user.setActive(active);

            } else {
                user=null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void saveUser(User user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO wauser(USERNAME, EMAIL, PASSWORD, ROLES, ACTIVE) VALUES(?,?,?,?,?)")){
            ps.setString(1,user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRoles());
            ps.setInt(5,user.getActive());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //To add testusers in DBinit....
    public void saveAll(List<User> users) {
        for(User user:users) {
            saveUser(user);
        }
    }

}
