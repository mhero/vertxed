package com.mac.rx.verticle;

import org.apache.http.HttpStatus;

import com.mac.rx.movie.MovieRepository;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class Routes {

	private static final String ALL_MOVIES_ENDPOINTS = "/protected/api/movies*";
	private static final String MOVIES_ENDPOINT = "/protected/api/movies";
	private static final String MOVIES_ENDPOINT_WITH_ID = "/protected/api/movies/:id";

	public Router createRoutes(Vertx vertx, MovieRepository movieRepository) {
		Router router = Router.router(vertx);

		router.route().handler(BodyHandler.create());

		JWTAuth authProvider = new JwtProvider().createJwtProvider(vertx);
		routeLogin(router, authProvider);

		router.route("/protected/*").handler(JWTAuthHandler.create(authProvider));

		router.route("/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/html").end("<h1>Hello Vert.x 3!</h1>");
		});

		router.route("/assets/*").handler(StaticHandler.create("assets"));

		router.route(ALL_MOVIES_ENDPOINTS).handler(CorsHandler.create("*").allowedHeaders(Headers.allowedHeaders)
				.allowedMethods(HttpMethods.allowedMethods));
		router.get(MOVIES_ENDPOINT).handler(movieRepository::getAll);
		router.post(MOVIES_ENDPOINT).handler(movieRepository::addOne);
		router.get(MOVIES_ENDPOINT_WITH_ID).handler(movieRepository::getOne);
		router.put(MOVIES_ENDPOINT_WITH_ID).handler(movieRepository::updateOne);
		router.delete(MOVIES_ENDPOINT_WITH_ID).handler(movieRepository::deleteOne);
		return router;
	}

	private void routeLogin(Router router, JWTAuth authProvider) {

		router.route("/login").handler(routingContext -> {
			final JsonObject jsonBody = routingContext.getBodyAsJson();

			// this is an example, authentication should be done with another provider...
			if ("marco".equals(jsonBody.getString("username")) && "secret".equals(jsonBody.getString("password"))) {
				routingContext.response().end(authProvider.generateToken(new JsonObject().put("sub", "marco")));
			} else {
				routingContext.fail(HttpStatus.SC_UNAUTHORIZED);
			}
		});

	}

}
