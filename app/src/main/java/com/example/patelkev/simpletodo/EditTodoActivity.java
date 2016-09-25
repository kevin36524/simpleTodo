package com.example.patelkev.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class EditTodoActivity extends AppCompatActivity {

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

        todoItem = (TodoItem) getIntent().getSerializableExtra("todoItem");
        itemIndex = getIntent().getIntExtra("itemIndex", 0);
        editText.setText(todoItem.title);
        checkBox.setChecked(todoItem.isDone);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                // Pass relevant data back as a result
                todoItem.title = editText.getText().toString();
                todoItem.isDone = checkBox.isChecked();
                data.putExtra("todoItem", todoItem);
                data.putExtra("itemIndex", itemIndex);
                // Activity finished ok, return the data
                setResult(200, data); // set result code and bundle data for response
                finish();
            }
        });
    }

}
