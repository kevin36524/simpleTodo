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
    Todo todo;
    int itemIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        editText = (EditText) findViewById(R.id.editText);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        saveButton = (Button) findViewById(R.id.button);

        todo = (Todo) getIntent().getSerializableExtra(intent_todo_item);
        itemIndex = getIntent().getIntExtra(intent_todo_item_index, 0);
        editText.setText(todo.title);
        checkBox.setChecked(todo.status == Todo.TodoStatus.DONE);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                // Pass relevant data back as a result
                todo.title = editText.getText().toString();
                todo.status = checkBox.isChecked() ? Todo.TodoStatus.DONE : Todo.TodoStatus.PENDING;
                data.putExtra(intent_todo_item, todo);
                data.putExtra(intent_todo_item_index, itemIndex);
                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish();
            }
        });
    }

}
