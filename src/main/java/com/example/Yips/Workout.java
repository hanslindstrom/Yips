package com.example.Yips;

import org.springframework.format.annotation.DateTimeFormat;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class Workout implements Comparable<Workout> {



    private Long id;
    private String name;
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String type;
    private int time;
    private String place;
    private String description;
    private String category;
    private String newDoingDone;
    private int daysToWorkout;


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


//Not working.... Error with getting the specific workouts date...
    public int getDaysToWorkout() {
        LocalDate now2 = LocalDate.now();
        System.out.println("this.Date" + this.date);
        System.out.println("now 2 = " + now2);
        System.out.println(this.date.compareTo(now2));

      /*  LocalDate now = LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        workout.setDate(now);*/
        return 2; //compareTo(workout);
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

    public String getNewDoingDone() {
        return newDoingDone;
    }

    public void setNewDoingDone(String newDoingDone) {
        this.newDoingDone = newDoingDone;
    }

    @Override
    public int compareTo(Workout workout) {
        return this.date.compareTo(workout.getDate());
    }

}
