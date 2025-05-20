/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.rx.movie;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author marco
 */
@SuppressWarnings("unused")
public class MovieRepository {

	public static final String COLLECTION = "movies";
	private final MongoClient mongo;
	private final Response response;

	public MovieRepository(MongoClient mongo) {
		this.mongo = mongo;
		this.response = new Response();
	}

	public void addOne(RoutingContext routingContext) {
		final Movie movie = Json.decodeValue(routingContext.body().asString(), Movie.class);

		mongo.insert(COLLECTION, movie.toJson()).onSuccess(id -> {
			movie.setId(id);
			response.succesful(routingContext, movie);
		}).onFailure(err -> {
			response.failed(routingContext, 500, "Failed to insert movie: " + err.getMessage());
		});
	}

	public void getOne(RoutingContext routingContext) {
		final String id = getId(routingContext);
		if (id == null) {
			response.badRequest(routingContext, "Invalid ID");
			return;
		}

		mongo.findOne(COLLECTION, findById(id), null).onSuccess(result -> {
			if (result == null) {
				response.notFound(routingContext, "Movie not found");
				return;
			}
			Movie movie = new Movie(result);
			response.succesful(routingContext, movie);
		}).onFailure(err -> {
			response.failed(routingContext, 500, "Database error: " + err.getMessage());
		});
	}

	public void updateOne(RoutingContext routingContext) {
		final String id = getId(routingContext);
		final JsonObject json = routingContext.body().asJsonObject();

		if (id == null || json == null) {
			response.badRequest(routingContext, "Missing ID or body");
			return;
		}

		Movie movie = new Movie(id, json.getString("name"), json.getString("rate"));

		mongo.updateCollection(COLLECTION, findById(id), new JsonObject().put("$set", json)).onSuccess(updateResult -> {
			response.succesful(routingContext, movie);
		}).onFailure(err -> {
			response.failed(routingContext, 500, "Update failed: " + err.getMessage());
		});
	}

	public void deleteOne(RoutingContext routingContext) {
		final String id = getId(routingContext);
		if (id == null) {
			response.badRequest(routingContext, "Invalid ID");
			return;
		}

		mongo.removeDocument(COLLECTION, findById(id)).onSuccess(res -> {
			routingContext.response().setStatusCode(HttpStatus.SC_NO_CONTENT).end();
		}).onFailure(err -> {
			response.failed(routingContext, 500, "Delete failed: " + err.getMessage());
		});
	}

	public void getAll(RoutingContext routingContext) {
		mongo.find(COLLECTION, new JsonObject()).onSuccess(result -> {
			List<Movie> movies = result.stream().map(Movie::new).collect(Collectors.toList());
			response.succesful(routingContext, movies);
		}).onFailure(err -> {
			response.failed(routingContext, 500, "Failed to retrieve movies: " + err.getMessage());
		});
	}

	private JsonObject findById(String id) {
		return new JsonObject().put("_id", id);
	}

	protected String getId(RoutingContext routingContext) {
		return routingContext.request().getParam("id");
	}
}
