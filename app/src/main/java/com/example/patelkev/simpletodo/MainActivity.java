package com.example.patelkev.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<TodoItem> items;
    TodoItemAdapter itemsAdapter;
    ListView lvItems;
    PersistanceManager sharedPersistanceManager;
    private final int REQUEST_CODE = 20;
    private final int RESULT_OK = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPersistanceManager = PersistanceManager.sharedInstance();
        sharedPersistanceManager.applicationContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = sharedPersistanceManager.readItems();
        itemsAdapter = new TodoItemAdapter(this, R.layout.todo_cell, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        TodoItem newTodoItem = new TodoItem(itemText, TodoItem.TodoStatus.PENDING);
        itemsAdapter.add(newTodoItem);
        sharedPersistanceManager.writeItems(items);
        etNewItem.setText("");
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                sharedPersistanceManager.writeItems(items);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem currentItem = items.get(position);
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
            TodoItem todoItem = (TodoItem) data.getExtras().getSerializable(EditTodoActivity.intent_todo_item);
            int itemIndex = data.getExtras().getInt(EditTodoActivity.intent_todo_item_index);
            items.set(itemIndex, todoItem);
            itemsAdapter.notifyDataSetChanged();
            sharedPersistanceManager.writeItems(items);
        }
    }

}
