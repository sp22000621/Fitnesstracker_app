package com.example.fitnesstracker_app;

public class Goal {
    private String name;
    private String deadline;

    public Goal(String name, String deadline) {
        this.name = name;
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    public String getDeadline() {
        return deadline;
    }
}