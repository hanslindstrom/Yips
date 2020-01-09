package com.example.Yips;

public class Invite {

    private Long id;
    private Long groupid;
    private Long senderid;
    private Long recipientid;

    public Invite() {
    }

    public Invite(Long groupid, Long senderid, Long recipientid) {
        this.groupid = groupid;
        this.senderid = senderid;
        this.recipientid = recipientid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupid() {
        return groupid;
    }

    public void setGroupid(Long groupid) {
        this.groupid = groupid;
    }

    public Long getSenderid() {
        return senderid;
    }

    public void setSenderid(Long senderid) {
        this.senderid = senderid;
    }

    public Long getRecipientid() {
        return recipientid;
    }

    public void setRecipientid(Long recipientid) {
        this.recipientid = recipientid;
    }
}
