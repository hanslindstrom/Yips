package com.example.Yips;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Workout {

    private Long id;
    private String name;
    private Date date;
    private int time;
    private String place;
    private String description;
    private String category;


    public Workout(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public Workout(String name, Date date, int time, String place, String description, String category) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.place = place;
        this.description = description;
        this.category = category;
    }

    public Workout() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
