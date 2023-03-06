package com.mac.rx.verticle;

import org.apache.http.HttpStatus;

import com.mac.rx.movie.MovieRepository;

import io.vertx.config.ConfigRetriever;
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
	private final ConfigRetriever configRetriever;
	private static final String USER = "user_name";
	private static final String USER_PASSWORD = "user_password";
	private final Router router;
	private final JWTAuth authProvider;

	public Routes(Router router, JWTAuth authProvider, ConfigRetriever configRetriever) {
		this.configRetriever = configRetriever;
		this.router = router;
		this.authProvider = authProvider;
	}

	public Router createRoutes(MovieRepository movieRepository) {

		router.route().handler(BodyHandler.create());

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
			// this is an example, authentication should be done with another provider...
			this.configRetriever.getConfig(json -> {
				JsonObject configFile = json.result();

				if (validateAuth(configFile, routingContext.body().asJsonObject())) {
					routingContext.response().end(authProvider.generateToken(tokenGenerator(configFile)));
				} else {
					routingContext.fail(HttpStatus.SC_UNAUTHORIZED);
				}
			});

		});

	}

	private JsonObject tokenGenerator(JsonObject configFile) {
		return new JsonObject().put("sub", configFile.getString(USER));
	}

	private boolean validateAuth(JsonObject configFile, JsonObject requestBody) {
		return configFile.getString(USER).equals(requestBody.getString(USER))
				&& configFile.getString(USER_PASSWORD).equals(requestBody.getString(USER_PASSWORD));
	}

}
