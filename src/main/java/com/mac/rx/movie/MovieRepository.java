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

import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.HttpStatus;

/**
 *
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

		mongo.insert(COLLECTION, movie.toJson(), results -> {
			if (results.succeeded()) {
				movie.setId(results.result());
				response.succesful(routingContext, movie);
			}
		});
	}

	public void getOne(RoutingContext routingContext) {
		final String id = getId(routingContext);
		if (id == null) {
			response.badRequest(routingContext);
			return;
		}

		mongo.findOne(COLLECTION, findById(id), null, results -> {
			if (results.failed() || results.result() == null) {
				response.notFound(routingContext);
				return;
			}
			Movie movie = new Movie(results.result());
			response.succesful(routingContext, movie);

		});

	}

	public void updateOne(RoutingContext routingContext) {
		final String id = getId(routingContext);
		final JsonObject json = routingContext.body().asJsonObject();

		if (id == null || json == null) {
			response.badRequest(routingContext);
			return;
		}

		Movie movie = new Movie(id, json.getString("name"), json.getString("rate"));

		mongo.updateCollection(COLLECTION, findById(id), new JsonObject().put("$set", json), results -> {
			if (results.failed()) {
				response.notFound(routingContext);
				return;
			}
			response.succesful(routingContext, movie);

		});

	}

	public void deleteOne(RoutingContext routingContext) {
		final String id = getId(routingContext);
		if (id == null) {
			response.badRequest(routingContext);
			return;
		}
		mongo.removeDocument(COLLECTION, findById(id),
				results -> routingContext.response().setStatusCode(HttpStatus.SC_NO_CONTENT).end());
	}

	public void getAll(RoutingContext routingContext) {
		mongo.find(COLLECTION, new JsonObject(), results -> {
			List<Movie> movies = results.result().stream().map(Movie::new).collect(Collectors.toList());
			response.succesful(routingContext, movies);
		});
	}

	private JsonObject findById(String id) {
		return new JsonObject().put("_id", id);
	}

	protected String getId(RoutingContext routingContext) {
		return routingContext.request().getParam("id");
	}
}
