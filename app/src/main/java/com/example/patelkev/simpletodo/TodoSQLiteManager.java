package com.example.patelkev.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by patelkev on 9/25/16.
 */

public class TodoSQLiteManager extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "todoDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TODO = "todos";

    // Post Table Columns
    private static final String KEY_TODO_ID = "id";
    private static final String KEY_TODO_TITLE = "title";
    private static final String KEY_TODO_STATUS = "status";

    private static TodoSQLiteManager sInstance;

    public static synchronized TodoSQLiteManager sharedInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new TodoSQLiteManager(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private TodoSQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOS_TABLE = "CREATE TABLE " + TABLE_TODO +
                "(" +
                KEY_TODO_ID + " TEXT PRIMARY KEY," + // Define a primary key
                KEY_TODO_TITLE + " TEXT," + // Define a foreign key
                KEY_TODO_STATUS + " TEXT" + // Define a foreign key
                ")";

        db.execSQL(CREATE_TODOS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            onCreate(db);
        }
    }

    // Insert a post into the database
    public void addOrUpdateTodo(TodoItem todo) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).

            ContentValues values = new ContentValues();
            values.put(KEY_TODO_ID, todo.id);
            values.put(KEY_TODO_TITLE, todo.title);
            values.put(KEY_TODO_STATUS, todo.status.name());

            int rows = db.update(TABLE_TODO, values, KEY_TODO_ID + "= ?", new String[]{todo.id});
            if (rows == 0) {
                db.insertOrThrow(TABLE_TODO, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<TodoItem> getAllTodos() {
        ArrayList<TodoItem> todos = new ArrayList<>();

        // SELECT * FROM ZFULLMESSAGE ORDER BY ZICID DESC
        String query =
                String.format("SELECT * FROM %s ORDER BY %s DESC",
                        TABLE_TODO,
                        KEY_TODO_ID);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(cursor.getColumnIndex(KEY_TODO_TITLE));
                    TodoItem.TodoStatus status = TodoItem.TodoStatus.valueOf(cursor.getString(cursor.getColumnIndex(KEY_TODO_STATUS)));
                    TodoItem todo = new TodoItem(title, status);
                    todo.id = cursor.getString(cursor.getColumnIndex(KEY_TODO_ID));
                    todos.add(todo);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return todos;

    }

    public void deleteTodo(TodoItem todo) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        String whereClause = String.format("WHERE %s == ?", KEY_TODO_ID);
        try {
            db.delete(TABLE_TODO,whereClause, new String[]{todo.id});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while deleting todo");
        } finally {
            db.endTransaction();
        }
    }

    // Delete all posts and users in the database
    public void deleteAllTodos() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_TODO, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }
}