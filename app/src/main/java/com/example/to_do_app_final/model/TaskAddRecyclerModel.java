package com.example.to_do_app_final.model;

public class TaskAddRecyclerModel {
    private  String task;

    public TaskAddRecyclerModel(String task) {
        this.task = task;

    }
    public String getTask() {
        return task;
    }

    public void setTask(String newTask) {

        this.task = newTask;
    }
}
