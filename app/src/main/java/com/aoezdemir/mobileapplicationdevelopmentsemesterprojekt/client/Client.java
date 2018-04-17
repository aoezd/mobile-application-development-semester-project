package com.aoezdemir.mobileapplicationdevelopmentsemesterprojekt.client;

import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public abstract class Client<T> {

    protected final static String API_URL = "http://localhost:8080";

    public abstract List<T> get();

    public abstract T get(Long id);

    public abstract void create(T object);

    public abstract void create(List<T> objects);

    public abstract void update(T object);

    public abstract void delete();

    public abstract void delete(Long id);

    public abstract void delete(T object);

    protected enum ApiResource {
        TODO("todos"), USER("users");

        private String resource;

        ApiResource(String resource) {
            this.resource = resource;
        }

        private WebTarget getWebTarget() {
            return ClientBuilder.newClient().target(API_URL).path("api").path(this.resource);
        }

        public String getResource() {
            return this.resource;
        }

        public Invocation.Builder getRequestBuilder() {
            return this.getWebTarget().request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);
        }

        public Invocation.Builder getRequestBuilder(Long id) {
            return this.getWebTarget().path(id.toString()).request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);
        }
    }
}