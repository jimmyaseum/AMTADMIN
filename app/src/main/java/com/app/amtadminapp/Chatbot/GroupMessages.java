package com.app.amtadminapp.Chatbot;

public class GroupMessages
{
    private String date, from, message, name, time, type, unreaduid;

    public GroupMessages(){}

    public GroupMessages(String date, String from, String message, String name, String time, String type, String unreaduid) {
        this.date = date;
        this.from = from;
        this.message = message;
        this.name = name;
        this.time = time;
        this.type = type;
        this.unreaduid = unreaduid;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnreaduid() {
        return unreaduid;
    }

    public void setUnreaduid(String unreaduid) {
        this.unreaduid = unreaduid;
    }
}
