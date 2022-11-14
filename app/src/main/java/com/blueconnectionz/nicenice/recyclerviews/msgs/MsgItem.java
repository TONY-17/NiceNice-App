package com.blueconnectionz.nicenice.recyclerviews.msgs;

public class MsgItem {
    private String owner;
    private String lastMessage;
    private String time;
    private int profileImage;
    boolean rejected;
    boolean accepted;
    boolean newMessage;

    public MsgItem(String owner, String lastMessage, String time, int profileImage, boolean rejected, boolean accepted,boolean newMessage) {
        this.owner = owner;
        this.lastMessage = lastMessage;
        this.time = time;
        this.profileImage = profileImage;

        this.newMessage = newMessage;
        this.accepted = accepted;
        this.rejected = rejected;

    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isNewMessage() {
        return newMessage;
    }

    public void setNewMessage(boolean newMessage) {
        this.newMessage = newMessage;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }
}
