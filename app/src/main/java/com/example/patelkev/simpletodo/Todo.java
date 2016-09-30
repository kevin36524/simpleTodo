package com.example.patelkev.simpletodo;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
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

    public static ArrayList<Todo> getTodoList(Context appContext, MainActivity.FilterState filterState) {
        ArrayList<Todo> toods= new ArrayList<Todo>();
        return TodoSQLiteManager.sharedInstance(appContext).getTodosForFilterState(filterState);
    }
}
