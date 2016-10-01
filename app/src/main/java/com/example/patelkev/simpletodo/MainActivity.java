package com.example.patelkev.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TodoItemAdapter.TodoItemInterface {

    enum FilterState {
        FILTER_STATE_ALL, FILTER_STATE_DONE, FILTER_STATE_PENDING
    }

    TodoItemAdapter itemsAdapter;
    RecyclerView rvItems;
    TodoSQLiteManager sharedTodoSqliteManager;
    Toolbar toolbar;
    private final int REQUEST_CODE = 20;
    private final int RESULT_OK = 200;
    private TextView toolbar_title;
    private FilterState filterState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sharedTodoSqliteManager = TodoSQLiteManager.sharedInstance(getApplicationContext());

        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);

        // TODO : Store filterState and get it from Shared Preferences
        setFilterState(FilterState.FILTER_STATE_ALL);

        itemsAdapter = new TodoItemAdapter(this, filterState, this);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
    }

    /* TodoInterface Delegate methods */
    @Override
    public void tappedTodo(Todo todo, int position) {
        Intent i = new Intent(MainActivity.this, EditTodoActivity.class);
        i.putExtra(EditTodoActivity.intent_todo_item, todo);
        i.putExtra(EditTodoActivity.intent_todo_item_index, position);
        startActivityForResult(i, REQUEST_CODE);
    }

    public void saveModifiedItem (Todo todo) {
        sharedTodoSqliteManager.addOrUpdateTodo(todo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            Todo todo = (Todo) data.getExtras().getSerializable(EditTodoActivity.intent_todo_item);
            int itemIndex = data.getExtras().getInt(EditTodoActivity.intent_todo_item_index);
            itemsAdapter.modifyTodo(todo, itemIndex);
            sharedTodoSqliteManager.addOrUpdateTodo(todo);
        }
    }

    @Override
    public void deleteTodo(Todo todo) {
        sharedTodoSqliteManager.deleteTodo(todo);
        Log.d(this.getClass().getName(), "Delete todo request received");
    }


    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Todo newTodo = new Todo(itemText);
        itemsAdapter.addTodo(newTodo);
        sharedTodoSqliteManager.addOrUpdateTodo(newTodo);
        etNewItem.setText("");
    }

    // Toolbar methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onFilterAction(MenuItem item) {
       showFilterFragment();
    }

    public void setFilterState (FilterState newfilterState) {
        String filterStateName = getResources().getText(R.string.todo_navbar_title) + "-";
        switch (newfilterState) {
            case FILTER_STATE_ALL:
                filterStateName += getResources().getString(R.string.filter_all);
                break;
            case FILTER_STATE_DONE:
                filterStateName += getResources().getString(R.string.filter_done);
                break;
            case FILTER_STATE_PENDING:
                filterStateName += getResources().getString(R.string.filter_pending);
                break;
        }
        filterState = newfilterState;
        toolbar_title.setText(filterStateName);
    }

    private void showFilterFragment() {

        FragmentManager fm = getSupportFragmentManager();
        FilterTodoFragment filterTodoFragment = FilterTodoFragment.newInstance(filterState, new FilterTodoFragment.FilterTodoFragmentDelegate() {
            @Override
            public void filterFragmentDismissedWithFilterState(FilterState newfilterState) {
                setFilterState(newfilterState);
                itemsAdapter.setFilterState(newfilterState);
            }
        });
        filterTodoFragment.show(fm, "filter_fragment");
    }

}
