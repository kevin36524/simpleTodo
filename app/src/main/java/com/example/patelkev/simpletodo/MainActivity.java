package com.example.patelkev.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Todo> items;
    TodoItemAdapter itemsAdapter;
    ListView lvItems;
    TodoSQLiteManager sharedTodoSqliteManager;
    private final int REQUEST_CODE = 20;
    private final int RESULT_OK = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedTodoSqliteManager = TodoSQLiteManager.sharedInstance(getApplicationContext());

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = sharedTodoSqliteManager.getAllTodos();
        itemsAdapter = new TodoItemAdapter(this, R.layout.todo_cell, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Todo newTodo = new Todo(itemText, Todo.TodoStatus.PENDING);
        itemsAdapter.add(newTodo);
        sharedTodoSqliteManager.addOrUpdateTodo(newTodo);
        etNewItem.setText("");
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Todo itemToRemove = items.get(position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                sharedTodoSqliteManager.deleteTodo(itemToRemove);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Todo currentItem = items.get(position);
                Intent i = new Intent(MainActivity.this, EditTodoActivity.class);
                i.putExtra(EditTodoActivity.intent_todo_item, currentItem);
                i.putExtra(EditTodoActivity.intent_todo_item_index, position);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            Todo todo = (Todo) data.getExtras().getSerializable(EditTodoActivity.intent_todo_item);
            int itemIndex = data.getExtras().getInt(EditTodoActivity.intent_todo_item_index);
            items.set(itemIndex, todo);
            itemsAdapter.notifyDataSetChanged();
            sharedTodoSqliteManager.addOrUpdateTodo(todo);
        }
    }
    // Toolbar methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
