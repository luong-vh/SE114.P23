package com.example.uddd_b3;


import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.UUID;

public class TodoItem implements Serializable {
    private  String id;
    private String title;
    private String date;
    private String description;
    private boolean done;
    private  boolean selected;

    public TodoItem(String title, @Nullable String description, String date, boolean done) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.done = done;
        this.id = UUID.randomUUID().toString();
    }
    public TodoItem(String id,String title, String description, String date, boolean done) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.done = done;
        this.id = id;
    }
    public  String getId()
    {
        return id;
    }
    public  void setId(String id)
    {
        this.id = id;
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
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
