package com.example.fitnesstracker_app;

import java.util.ArrayList;

public class GoalManager {
    private static GoalManager instance;
    private static final ArrayList<Goal> goals = new ArrayList<>();

    private GoalManager() {
        // Private constructor to prevent instantiation from outside
    }

    public static GoalManager getInstance() {
        if (instance == null) {
            synchronized (GoalManager.class) {
                if (instance == null) {
                    instance = new GoalManager();
                }
            }
        }
        return instance;
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
    }

    public void clearGoals() {
        goals.clear();
    }

    public ArrayList<Goal> getGoals() {
        return new ArrayList<>(goals);
    }
}