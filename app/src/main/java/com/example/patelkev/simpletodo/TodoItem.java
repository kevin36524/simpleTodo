package com.example.patelkev.simpletodo;

import java.io.Serializable;
import java.util.UUID;

public class TodoItem implements Serializable {
    public enum TodoStatus {
        PENDING, DONE
    }

    public String title;
    public TodoStatus status;
    public String id;

    public TodoItem(String title, TodoStatus status) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.status = status;
    }


}
