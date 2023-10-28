package com.example.lab06b;

import java.io.Serializable;

public class Data implements Serializable {
    private String Title,Time,Room,Date;

    public boolean isState() {
        return State;
    }

    public void setState(boolean state) {
        State = state;
    }

    private boolean State;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public Data(String title, String room, String date, String time, boolean state) {
        this.Title = title;
        Room = room;
        Date = date;
        Time = time;
        this.State = state;
    }
}
