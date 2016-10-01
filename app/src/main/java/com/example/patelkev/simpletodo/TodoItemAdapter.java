package com.example.patelkev.simpletodo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import static android.graphics.Paint.STRIKE_THRU_TEXT_FLAG;
import static com.example.patelkev.simpletodo.Todo.getTodoList;

/**
 * Created by patelkev on 9/25/16.
 */

public class TodoItemAdapter extends  RecyclerView.Adapter<TodoItemAdapter.ViewHolder> {


    public static interface TodoItemInterface {
        public void tappedTodo (Todo todo, int position);
        public void deleteTodo (Todo todo);
        public void saveModifiedItem (Todo todo);
    }

    private ArrayList<Todo> mtodos;
    private Context mContext;
    private MainActivity.FilterState filterState;
    private TodoItemInterface adapterDelegate;

    public TodoItemAdapter(Context context, MainActivity.FilterState filterState, TodoItemInterface adapterDelegate) {
        this.adapterDelegate = adapterDelegate;
        mContext = context;
        this.filterState = filterState;
        mtodos = getTodoList(context, filterState);
    }

    public void setFilterState(MainActivity.FilterState newFilterState) {
        this.filterState = newFilterState;
        mtodos.clear();
        mtodos.addAll(Todo.getTodoList(getContext(), filterState));
        this.notifyDataSetChanged();
    }

    public void addTodo(Todo newTodo) {
        mtodos.add(newTodo);
        this.notifyItemInserted(mtodos.size());
    }

    public void modifyTodo(Todo todo, int itemIndex) {
        mtodos.set(itemIndex, todo);
        this.notifyItemChanged(itemIndex);
    }

    private Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView itemView;
        private CheckBox checkBox;
        private TodoItemAdapter adapter;

        public ViewHolder(View view, TodoItemAdapter adapter) {
            super(view);

            this.adapter = adapter;
            itemView = (TextView) view.findViewById(R.id.itemView);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Todo activeTodo = adapter.mtodos.get(position);

            switch (v.getId()) {
                case R.id.itemView :
                    adapter.adapterDelegate.tappedTodo(activeTodo, position);
                    break;
                case R.id.checkBox:
                    activeTodo.toggleTodoStatus();
                    adapter.notifyItemChanged(position);
                    adapter.adapterDelegate.saveModifiedItem(activeTodo);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.itemView :
                    int position = getAdapterPosition();
                    Todo itemToBeDeleted = adapter.mtodos.get(position);
                    adapter.mtodos.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.adapterDelegate.deleteTodo(itemToBeDeleted);
                    break;
            }
            return true;
        }
    }

    @Override
    public TodoItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View todoView = inflater.inflate(R.layout.todo_cell, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(todoView, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodoItemAdapter.ViewHolder holder, int position) {
        Todo todo = mtodos.get(position);

        holder.itemView.setText(todo.title);
        int paintFlags = holder.itemView.getPaintFlags() & (~STRIKE_THRU_TEXT_FLAG);
        if (todo.status == Todo.TodoStatus.DONE) {
            paintFlags = paintFlags | STRIKE_THRU_TEXT_FLAG;
        }
        holder.itemView.setPaintFlags(paintFlags);
        holder.checkBox.setChecked(todo.isDone());
    }

    @Override
    public int getItemCount() {
        return mtodos.size();
    }
}
