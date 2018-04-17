package com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.client;

import com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.model.Todo;

import junit.framework.Assert;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientTodoTest {

    private final static Long DEFAULT_ID = 1234567L;
    private final static String DEFAULT_NAME = "Some fancy sophisticated name";
    private final static String DEFAULT_DESCRIPTION = "lorem ipsum dolor";

    private ClientTodo client = new ClientTodo();
    private Todo fooTodo = new Todo(DEFAULT_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION, 0L,
            false, false, null);

    @Test
    public void t1_getTest() {
        List<Todo> todos = this.client.get();
        Assert.assertFalse(todos == null);
        Assert.assertFalse(todos.isEmpty());
    }

    @Test
    public void t2_getByIdTest() {
        Todo todo = this.client.get(0L);
        Assert.assertFalse(todo == null);
        Assert.assertEquals(0L, todo.getId().longValue());
    }

    @Test
    public void t3_getByNonExistIdTest() {
        Assert.assertTrue(this.client.get(-1L) == null);
    }

    @Test
    public void t4_createTest() {
        this.client.create(this.fooTodo);
        Todo fooTodo1 = this.client.get(this.fooTodo.getId());
        Assert.assertFalse(fooTodo1 == null);
        Assert.assertTrue(fooTodo1.equals(this.fooTodo));
    }

    @Test
    public void t5_updateTest() {
        Assert.assertFalse(this.fooTodo.isDone());
        this.fooTodo.setDone(true);
        this.client.update(this.fooTodo);
        Assert.assertTrue(this.client.get(this.fooTodo.getId()).isDone());
    }

    @Test
    public void t6_deleteByIdTest() {
        this.client.delete(this.fooTodo);
        Assert.assertTrue(this.client.get(this.fooTodo.getId()) == null);
    }

    @Test
    public void t7_deleteCreateAllTest() {
        List<Todo> fooTodos = this.client.get();
        Integer size = fooTodos.size();
        Assert.assertFalse(fooTodos.isEmpty());
        this.client.delete();
        Assert.assertTrue(this.client.get().isEmpty());
        this.client.create(fooTodos);
        Assert.assertEquals(size.intValue(), this.client.get().size());
    }
}
