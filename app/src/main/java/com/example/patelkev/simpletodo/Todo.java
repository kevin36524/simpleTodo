package com.example.patelkev.simpletodo;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Todo implements Serializable {
    public enum TodoStatus {
        PENDING, DONE
    }
    public enum TodoPriority {
        HIGH, MEDIUM, LOW
    }

    public String title;
    public TodoStatus status;
    public String id;
    public TodoPriority priority;

    public Todo(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.status = TodoStatus.PENDING;
        this.priority = TodoPriority.LOW;
    }

    public void  toggleTodoStatus() {
        this.status = this.isDone() ? TodoStatus.PENDING : TodoStatus.DONE;
    }

    public boolean isDone() {
        return  (this.status == TodoStatus.DONE);
    }

    public static ArrayList<Todo> getTodoList(Context appContext, MainActivity.FilterState filterState) {
        ArrayList<Todo> toods= new ArrayList<Todo>();
        return TodoSQLiteManager.sharedInstance(appContext).getTodosForFilterState(filterState);
    }
}
