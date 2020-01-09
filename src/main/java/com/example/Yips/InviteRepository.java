package com.example.Yips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
