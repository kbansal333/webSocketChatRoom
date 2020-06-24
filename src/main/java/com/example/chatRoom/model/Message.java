package com.example.chatRoom.model;

public class Message {
    private String content;
    private String sender;
    private int onlineCount;
    private int type;
    public Message(String content,String sender,int onlineCount,int type) {
        this.content = content;
        this.sender = sender;
        this.onlineCount = onlineCount;
        this.type = type;
    }

    public Message() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String message) {
        this.content = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
