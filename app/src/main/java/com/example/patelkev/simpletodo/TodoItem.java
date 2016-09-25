package com.example.patelkev.simpletodo;

import java.io.Serializable;

/**
 * Created by patelkev on 9/24/16.
 */

public class TodoItem implements Serializable {
    public String title;
    public Boolean isDone;

    public TodoItem(String title, Boolean isDone) {
        this.title = title;
        this.isDone = isDone;
    }
}
