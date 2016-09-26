package com.example.patelkev.simpletodo;

import java.io.Serializable;
import java.util.UUID;

public class Todo implements Serializable {
    public enum TodoStatus {
        PENDING, DONE
    }

    public String title;
    public TodoStatus status;
    public String id;

    public Todo(String title, TodoStatus status) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.status = status;
    }


}
