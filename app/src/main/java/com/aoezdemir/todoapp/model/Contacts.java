package com.aoezdemir.todoapp.model;

import android.content.ContentValues;

import com.aoezdemir.todoapp.crud.local.TodoDBHelper;

import java.util.ArrayList;
import java.util.List;

public class Contacts {

    private Long todoId;
    private List<String> contacts;

    public Contacts() {
        super();
    }

    public Contacts(Long todoId, List<String> contacts) {
        this.todoId = todoId;
        this.contacts = contacts;
    }

    public Long getTodoId() {
        return todoId;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public List<ContentValues> toContentValues() {
        List<ContentValues> cvs = new ArrayList<>();
        for (String contact : contacts) {
            ContentValues cv = new ContentValues();
            cv.put(TodoDBHelper.COL_CONTACTS_NAME, contact);
            cv.put(TodoDBHelper.COL_CONTACTS_TODO_ID, todoId);
        }
        return cvs;
    }
}
