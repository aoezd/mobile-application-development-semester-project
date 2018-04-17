package com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.client;

import com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.model.Todo;

import org.apache.log4j.Logger;

import java.util.List;

import javax.ws.rs.core.GenericType;

public class ClientTodo extends Client<Todo> {

    private static final Logger LOG = Logger.getLogger(ClientTodo.class);

    private ApiResource todoResource;

    public ClientTodo() {
        this.todoResource = ApiResource.TODO;
    }

    /**
     * Gets all todos from the web app.
     *
     * @return List of all todos
     */
    @Override
    public List<Todo> get() {
        return this.todoResource.getRequestBuilder().get()
                .readEntity(new GenericType<List<Todo>>() {
                });
    }

    /**
     * Gets a specific todo with a given id.
     *
     * @param id ID of desired todo
     * @return Todo with given ID
     */
    @Override
    public Todo get(Long id) {
        return id != null && id >= 0 ?
                this.todoResource.getRequestBuilder(id).get().readEntity(Todo.class) :
                null;
    }

    /**
     * Updates (creates if not existing) a given todo.
     *
     * @param todo Todo with will be updated
     */
    @Override
    public void update(Todo todo) {
        if (todo != null) {
            this.todoResource.getRequestBuilder(todo.getId()).put(javax.ws.rs.client.Entity.json(todo));
        }
    }

    /**
     * Creates a new todo.
     *
     * @param todo Todo which will be created
     */
    @Override
    public void create(Todo todo) {
        if (todo != null) {
            this.todoResource.getRequestBuilder().post(javax.ws.rs.client.Entity.json(todo));
        }
    }

    /**
     * Creates all todos which are in the given todo list.
     *
     * @param todos List of todos to be created
     */
    @Override
    public void create(List<Todo> todos) {
        if (todos != null && !todos.isEmpty()) {
            for (Todo todo : todos) {
                this.create(todo);
            }
        }
    }

    /**
     * Deletes all todos.
     */
    @Override
    public void delete() {
        this.todoResource.getRequestBuilder().delete();
    }

    /**
     * Deletes a specific todo based on the given id.
     *
     * @param id ID of todo which should be deleted
     */
    @Override
    public void delete(Long id) {
        this.todoResource.getRequestBuilder(id).delete();
    }

    /**
     * Deletes the given todo.
     *
     * @param todo Todo to be deleted
     */
    @Override
    public void delete(Todo todo) {
        this.delete(todo.getId());
    }
}
