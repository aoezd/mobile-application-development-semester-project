package com.aoezdemir.todoapp.utils;

import com.aoezdemir.todoapp.model.Todo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TodoListSorter {

    private static final Comparator<Todo> DATE_COMPARATOR = (Todo t1, Todo t2) -> new Date(t1.getExpiry()).before(new Date(t2.getExpiry())) ? -1 :
            new Date(t1.getExpiry()).after(new Date(t2.getExpiry())) ? 1 : 0;
    private static final Comparator<Todo> FAVOURITE_COMPARATOR = (Todo t1, Todo t2) -> t1.isFavourite() && !t2.isFavourite() ? -1 :
            !t1.isFavourite() && t2.isFavourite() ? 1 : 0;

    public static List<Todo> sort(List<Todo> todos, boolean sortDate) {
        if (todos != null && todos.size() > 1) {
            todos.sort(DATE_COMPARATOR);

            // Filter all done todos
            List<Todo> doneTodos = new ArrayList<>();
            List<Todo> noneDoneTodos = new ArrayList<>();
            for (int i = 0; i < todos.size(); i++) {
                Todo todo = todos.get(i);
                if (todo.isDone()) {
                    doneTodos.add(todo);
                } else {
                    noneDoneTodos.add(todo);
                }
            }

            // Sort based on favourite or date
            doneTodos.sort(sortDate ? DATE_COMPARATOR : FAVOURITE_COMPARATOR);
            noneDoneTodos.sort(sortDate ? DATE_COMPARATOR : FAVOURITE_COMPARATOR);

            // Put all together
            doneTodos.addAll(noneDoneTodos);

            return doneTodos;
        }
        return todos;
    }
}