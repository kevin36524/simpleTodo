package com.example.patelkev.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class EditTodoActivity extends AppCompatActivity {

    public static final String intent_todo_item = "intent_todo_item";
    public static final String intent_todo_item_index = "intent_todo_item_index";
    private final int RESULT_OK = 200;

    EditText editText;
    CheckBox checkBox;
    Button saveButton;
    TodoItem todoItem;
    int itemIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        editText = (EditText) findViewById(R.id.editText);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        saveButton = (Button) findViewById(R.id.button);

        todoItem = (TodoItem) getIntent().getSerializableExtra(intent_todo_item);
        itemIndex = getIntent().getIntExtra(intent_todo_item_index, 0);
        editText.setText(todoItem.title);
        checkBox.setChecked(todoItem.status == TodoItem.TodoStatus.DONE);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                // Pass relevant data back as a result
                todoItem.title = editText.getText().toString();
                todoItem.status = checkBox.isChecked() ? TodoItem.TodoStatus.DONE : TodoItem.TodoStatus.PENDING;
                data.putExtra(intent_todo_item, todoItem);
                data.putExtra(intent_todo_item_index, itemIndex);
                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish();
            }
        });
    }

}
