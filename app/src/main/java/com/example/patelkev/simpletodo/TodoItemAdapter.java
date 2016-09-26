package com.example.patelkev.simpletodo;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.graphics.Paint.STRIKE_THRU_TEXT_FLAG;

/**
 * Created by patelkev on 9/25/16.
 */

public class TodoItemAdapter extends ArrayAdapter<TodoItem> {
    private static class ViewHolder {
        private TextView itemView;
    }

    public TodoItemAdapter(Context context, int textViewResourceId, ArrayList<TodoItem> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.todo_cell, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (TextView) convertView.findViewById(R.id.itemView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TodoItem item = getItem(position);
        if (item!= null) {
            // My layout has only one TextView
            // do whatever you want with your string and long
            viewHolder.itemView.setText(item.title);
            int paintFlags = viewHolder.itemView.getPaintFlags() & (~ STRIKE_THRU_TEXT_FLAG);
            if (item.status == TodoItem.TodoStatus.DONE) {
                paintFlags = paintFlags | Paint.STRIKE_THRU_TEXT_FLAG;
            }
            viewHolder.itemView.setPaintFlags(paintFlags);
        }

        return convertView;
    }
}
