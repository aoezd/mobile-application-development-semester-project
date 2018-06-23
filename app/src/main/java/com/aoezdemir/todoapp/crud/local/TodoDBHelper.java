package com.aoezdemir.todoapp.crud.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aoezdemir.todoapp.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Todos.db";
    private static final String TABLE_TODOS_NAME = "TODOS";
    private static final String TABLE_CONTACTS_NAME = "CONTACTS";
    public static final String COL_TODO_ID = "ID";
    public static final String COL_TODO_NAME = "NAME";
    public static final String COL_TODO_DESCRIPTION = "DESCRIPTION";
    public static final String COL_TODO_EXPIRY = "EXPIRY";
    public static final String COL_TODO_DONE = "DONE";
    public static final String COL_TODO_FAVOURITE = "FAVOURITE";
    public static final String COL_CONTACTS_NAME = "NAME";
    public static final String COL_CONTACTS_TODO_ID = "TODO_ID";
    private static final String QUERY_CREATE_TODOS = "create table " + TABLE_TODOS_NAME + "(" + COL_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + COL_TODO_NAME + " TEXT NOT NULL, " + COL_TODO_DESCRIPTION + " TEXT, " + COL_TODO_EXPIRY + " INTEGER NOT NULL, " + COL_TODO_DONE + " INTEGER NOT NULL, " + COL_TODO_FAVOURITE + " INTEGER NOT NULL)";
    private static final String QUERY_CREATE_CONTACTS = "create table " + TABLE_CONTACTS_NAME + "(" + COL_CONTACTS_NAME + " TEXT NOT NULL, " + COL_CONTACTS_TODO_ID + " INTEGER NOT NULL, FOREIGN KEY(TODO_ID) REFERENCES " + TABLE_TODOS_NAME + "(" + COL_TODO_ID + "))";

    public TodoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_CREATE_TODOS);
        db.execSQL(QUERY_CREATE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteAllTodos(db);
        onCreate(db);
    }

    public Todo getTodo(Long id) {
        Cursor result = this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_TODOS_NAME + " WHERE ID = " + id.toString(), null);
        if (result.moveToFirst()) {
            return Todo.createFrom(result);
        }
        return null;
    }

    public List<Todo> getAllTodos() {
        List<Todo> todos = new ArrayList<>();
        Cursor result = this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_TODOS_NAME, null);
        while(result.moveToNext()) {
            todos.add(Todo.createFrom(result));
        }
        return todos;
    }

    public boolean updateTodo(Todo newTodo) {
        boolean hasTodoUpdateSucceeded = this.getWritableDatabase().update(TABLE_TODOS_NAME, newTodo.toContentValues(), COL_TODO_ID + " = ?", new String[] { newTodo.getId().toString() }) != -1;
        // boolean hasContactsUpdateSucceeded = TODO FOR CONTACTS;
        return hasTodoUpdateSucceeded;
    }

    public boolean insertTodo(Todo todo) {
        boolean hasTodoInsertionSucceeded = this.getWritableDatabase().insert(TABLE_TODOS_NAME, null, todo.toContentValues()) != -1;
        // boolean hasContactsInsertionSucceeded = TODO FOR CONTACTS;
        return hasTodoInsertionSucceeded;
    }

    public boolean deleteTodo(Long id) {
        boolean hasTodoDeletionSucceeded = this.getWritableDatabase().delete(TABLE_TODOS_NAME, COL_TODO_ID + " = ?", new String[] { id.toString() }) == 1;
        // boolean hasContactsDeletionSucceeded = TODO FOR CONTACTS;
        return hasTodoDeletionSucceeded;
    }

    public void deleteAllTodos() {
        deleteAllTodos(this.getWritableDatabase());
    }

    public void deleteAllTodos(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS_NAME);
    }
}
