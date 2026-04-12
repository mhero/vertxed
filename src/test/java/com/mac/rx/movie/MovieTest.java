package com.mac.rx.movie;

import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void testConstructorWithNameAndRate() {
        Movie movie = new Movie("The Godfather", "9.2");

        assertEquals("The Godfather", movie.getName());
        assertEquals("9.2", movie.getRate());
        assertEquals("", movie.getId());
    }

    @Test
    void testConstructorWithIdNameAndRate() {
        Movie movie = new Movie("abc123", "The Godfather", "9.2");

        assertEquals("abc123", movie.getId());
        assertEquals("The Godfather", movie.getName());
        assertEquals("9.2", movie.getRate());
    }

    @Test
    void testEmptyConstructor() {
        Movie movie = new Movie();

        assertEquals("", movie.getId());
        assertNull(movie.getName());
        assertNull(movie.getRate());
    }

    @Test
    void testJsonConstructor() {
        JsonObject json = new JsonObject()
                .put("_id", "abc123")
                .put("name", "The Godfather")
                .put("rate", "9.2");

        Movie movie = new Movie(json);

        assertEquals("abc123", movie.getId());
        assertEquals("The Godfather", movie.getName());
        assertEquals("9.2", movie.getRate());
    }

    @Test
    void testToJsonWithId() {
        Movie movie = new Movie("abc123", "The Godfather", "9.2");
        JsonObject json = movie.toJson();

        assertEquals("abc123", json.getString("_id"));
        assertEquals("The Godfather", json.getString("name"));
        assertEquals("9.2", json.getString("rate"));
    }

    @Test
    void testToJsonWithoutId() {
        Movie movie = new Movie("The Godfather", "9.2");
        JsonObject json = movie.toJson();

        assertFalse(json.containsKey("_id"));
        assertEquals("The Godfather", json.getString("name"));
        assertEquals("9.2", json.getString("rate"));
    }

    @Test
    void testSetters() {
        Movie movie = new Movie();
        movie.setName("The Godfather");
        movie.setId("abc123");
        movie.setOrigin("9.2");

        assertEquals("The Godfather", movie.getName());
        assertEquals("abc123", movie.getId());
        assertEquals("9.2", movie.getRate());
    }
}