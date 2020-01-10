package com.example.Yips;

public class Exercise {
    private Long id;
    private String name;
    private int seconds;
    private int meters;
    private int calories;
    private int weight;
    private int reps;
    private int sets;
    private int cadence;

    public Exercise(String name, int seconds, int meters, int calories, int weight, int reps, int sets, int cadence) {
        this.name = name;
        this.seconds = seconds;
        this.meters = meters;
        this.calories = calories;
        this.weight = weight;
        this.reps = reps;
        this.sets = sets;
        this.cadence = cadence;
    }

    public Exercise() {
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



    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMeters() {
        return meters;
    }

    public void setMeters(int meters) {
        this.meters = meters;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getCadence() {
        return cadence;
    }

    public void setCadence(int cadence) {
        this.cadence = cadence;
    }
}
