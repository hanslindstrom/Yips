package com.example.Yips;

import java.sql.Date;
import java.time.LocalDate;

public class Goal {
    private Long id;
    private String name;
    private LocalDate deadline;
    private Long groupId;
    private boolean completed;


    public Goal(String name, boolean completed) {
        this.name = name;
        this.completed = completed;
    }

    public Goal() {

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

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
