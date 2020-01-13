package com.example.Yips;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Workout {



    private Long id;
    private String name;
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String type;
    private int time;
    private String place;
    private String description;
    private String category;


    public Workout(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public Workout(String name, LocalDate date, String type, int time, String place, String description, String category) {
        this.name = name;
        this.date = date;
        this.type=type;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
