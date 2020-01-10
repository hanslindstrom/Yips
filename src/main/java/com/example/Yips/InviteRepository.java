package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InviteRepository {

    @Autowired
    DataSource dataSource;


    public void saveInvite(Long groupId, Long senderId, Long recipientId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO INVITE(GROUPID, SENDERID, RECIPIENTID) VALUES(?,?,?)")){
            ps.setLong(1,groupId);
            ps.setLong(2, senderId);
            ps.setLong(3, recipientId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Invite> getAllInvitesWithUserId (Long userId) {
        ArrayList<Invite> invites = new ArrayList<>();
        Invite invite = new Invite();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM invite WHERE recipientid = ?")){
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Long groupId = rs.getLong("groupid");
                Long senderId = rs.getLong("senderid");
                Long inviteId = rs.getLong("id");
                invite.setId(inviteId);
                invite.setGroupid(groupId);
                invite.setSenderid(senderId);
                invite.setRecipientid(userId);
                invites.add(invite);
                System.out.println("This is a invite that we found: " + invite.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("This is the invite list we return to REST: " + invites.toString());
        return invites;
    }
    public void deleteInviteWithId (Long inviteId) {
        boolean rs = false;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM invite WHERE id = ?")){
            ps.setString(1, inviteId.toString());
            rs = ps.execute();
            System.out.println("rs 1" +     rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(rs);

    }

    public Invite getInviteWithId (Long inviteId) {
        Invite invite = new Invite();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM invite WHERE id = ?")){
            ps.setString(1, inviteId.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                long id = rs.getInt("id");
                long senderId = rs.getInt("senderid");
                long groupId = rs.getInt("groupid");
                long recipientid = rs.getInt("recipientid");

                invite.setId(id);
                invite.setRecipientid(recipientid);
                invite.setSenderid(senderId);
                invite.setGroupid(groupId);

            } else {
                invite=null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invite;
    }

}
