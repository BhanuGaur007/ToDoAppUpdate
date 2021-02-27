package com.example.to_do_app_final.model;

public class TaskHolderNameRecy {
    private String taskHolder;

    private String docId;

    public TaskHolderNameRecy(String taskHolder, String docId) {
        this.taskHolder = taskHolder;
        this.docId = docId;
    }

    public String getTaskHolder() {
        return taskHolder;
    }

    public void setTaskHolder(String taskHolder) {
        this.taskHolder = taskHolder;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
