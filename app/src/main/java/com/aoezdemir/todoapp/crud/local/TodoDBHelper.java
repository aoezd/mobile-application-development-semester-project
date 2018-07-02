package com.aoezdemir.todoapp.crud.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aoezdemir.todoapp.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoDBHelper extends SQLiteOpenHelper {

    public static final String COL_TODO_ID = "ID";
    public static final String COL_TODO_NAME = "NAME";
    public static final String COL_TODO_DESCRIPTION = "DESCRIPTION";
    public static final String COL_TODO_EXPIRY = "EXPIRY";
    public static final String COL_TODO_DONE = "DONE";
    public static final String COL_TODO_FAVOURITE = "FAVOURITE";
    public static final String COL_TODO_CONTACTS = "CONTACTS";

    private static final String DATABASE_NAME = "Todos.db";
    private static final String TABLE_TODOS_NAME = "TODOS";
    private static final String QUERY_CREATE_TODOS = "create table " + TABLE_TODOS_NAME + "(" + COL_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + COL_TODO_NAME + " TEXT NOT NULL, " + COL_TODO_DESCRIPTION + " TEXT, " + COL_TODO_EXPIRY + " INTEGER NOT NULL, " + COL_TODO_DONE + " INTEGER NOT NULL, " + COL_TODO_FAVOURITE + " INTEGER NOT NULL, " + COL_TODO_CONTACTS + " TEXT)";

    public TodoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_CREATE_TODOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteAllTodos(db);
        onCreate(db);
    }

    public List<Todo> getAllTodos() {
        List<Todo> todos = new ArrayList<>();
        Cursor result = this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_TODOS_NAME, null);
        while (result.moveToNext()) {
            todos.add(Todo.createFrom(result));
        }
        return todos;
    }

    public boolean updateTodo(Todo newTodo) {
        return this.getWritableDatabase().update(TABLE_TODOS_NAME, newTodo.toContentValues(), COL_TODO_ID + " = ?", new String[]{newTodo.getId().toString()}) != -1;
    }

    public boolean insertTodo(Todo todo) {
        return this.getWritableDatabase().insert(TABLE_TODOS_NAME, null, todo.toContentValues()) != -1;
    }

    public boolean insertAllTodos(List<Todo> todos) {
        for (int i = 0; i < todos.size(); i++) {
            if (!insertTodo(todos.get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean deleteTodo(Long id) {
        return this.getWritableDatabase().delete(TABLE_TODOS_NAME, COL_TODO_ID + " = ?", new String[]{id.toString()}) == 1;
    }

    public void deleteAllTodos() {
        deleteAllTodos(this.getWritableDatabase());
    }

    private void deleteAllTodos(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_TODOS_NAME);
    }
}