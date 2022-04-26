/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.rx.movie;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.HttpStatus;

import com.mac.rx.mongo.DatabaseClient;

/**
 *
 * @author marco
 */
@SuppressWarnings("unused")
public class MovieRepository extends DatabaseClient {

	public static final String COLLECTION = "movies";

	public MovieRepository(MongoClient mongo) {
		this.mongo = mongo;
	}

	public void addOne(RoutingContext routingContext) {
		final Movie movie = Json.decodeValue(routingContext.getBodyAsString(), Movie.class);

		mongo.insert(COLLECTION, movie.toJson(), results -> {
			if (results.succeeded()) {
				movie.setId(results.result());
				succesful(routingContext, movie);
			}
		});
	}

	public void getOne(RoutingContext routingContext) {
		final String id = getId(routingContext);
		if (id == null) {
			badRequest(routingContext);
			return;
		}

		mongo.findOne(COLLECTION, findById(id), null, results -> {
			if (results.failed() || results.result() == null) {
				notFound(routingContext);
				return;
			}
			Movie movie = new Movie(results.result());
			succesful(routingContext, movie);

		});

	}

	public void updateOne(RoutingContext routingContext) {
		final String id = getId(routingContext);
		final JsonObject json = routingContext.getBodyAsJson();
	
		if (id == null || json == null) {
			badRequest(routingContext);
			return;
		}
		
		Movie movie = new Movie(id, json.getString("name"), json.getString("rate"));

		mongo.updateCollection(COLLECTION, findById(id), new JsonObject().put("$set", json), results -> {
			if (results.failed()) {
				notFound(routingContext);
				return;
			}
			succesful(routingContext, movie);

		});

	}

	public void deleteOne(RoutingContext routingContext) {
		final String id = getId(routingContext);
		if (id == null) {
			badRequest(routingContext);
			return;
		}
		mongo.removeDocument(COLLECTION, findById(id),
				results -> routingContext.response().setStatusCode(HttpStatus.SC_NO_CONTENT).end());
	}

	public void getAll(RoutingContext routingContext) {
		mongo.find(COLLECTION, new JsonObject(), results -> {
			List<Movie> movies = results.result().stream().map(Movie::new).collect(Collectors.toList());
			succesful(routingContext, movies);
		});
	}

}
