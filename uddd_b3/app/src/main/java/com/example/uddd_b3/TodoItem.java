package com.example.uddd_b3;


import androidx.annotation.Nullable;

import java.io.Serializable;

public class TodoItem implements Serializable {
    private String title;
    private String date;
    private String description;
    private boolean done;

    public TodoItem(String title, @Nullable String description, String date, boolean done) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.done = done;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public String getDate() {
        return date;
    }
    public  void setDate(String d){
        this.date = d;
    }
    public String getDescription(){
        return description;
    }
    public  void setDescription(String description){
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
