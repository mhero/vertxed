package com.mac.rx;

import java.util.HashSet;
import java.util.Set;

import com.mac.rx.movie.MovieDAO;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class RestVerticle extends AbstractVerticle {

	public static final String COLLECTION = "movies";
	private MongoClient mongo;
	private MovieDAO movieDao;
	private final Integer port;

	public RestVerticle(Integer port) {
		this.port = port;
	}

	@Override
	public void start(Promise<Void> fut) {

		mongo = MongoClient.createShared(vertx, config());
		movieDao = new MovieDAO(mongo);

		movieDao.createSomeData((nothing) -> startWebApp((http) -> completeStartup(http, fut)), fut);
	}

	private void startWebApp(Handler<AsyncResult<HttpServer>> next) {
		Router router = Router.router(vertx);

		Set<String> allowHeaders = getHeaders();
		Set<HttpMethod> allowMethods = getMethods();

		router.route("/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/html").end("<h1>Hello Vert.x 3!</h1>");
		});

		router.route("/assets/*").handler(StaticHandler.create("assets"));

		router.route("/api/movies*").handler(BodyHandler.create());
		router.route("/api/movies*")
				.handler(CorsHandler.create("*").allowedHeaders(allowHeaders).allowedMethods(allowMethods));
		router.get("/api/movies").handler(movieDao::getAll);
		router.post("/api/movies").handler(movieDao::addOne);
		router.get("/api/movies/:id").handler(movieDao::getOne);
		router.put("/api/movies/:id").handler(movieDao::updateOne);
		router.delete("/api/movies/:id").handler(movieDao::deleteOne);

		vertx.createHttpServer().requestHandler(router).listen(config().getInteger("http.port", this.port),
				next::handle);
	}

	private Set<String> getHeaders() {
		Set<String> allowHeaders = new HashSet<>();
		allowHeaders.add("x-requested-with");
		allowHeaders.add("Access-Control-Allow-Origin");
		allowHeaders.add("origin");
		allowHeaders.add("Content-Type");
		allowHeaders.add("accept");
		return allowHeaders;
	}

	private Set<HttpMethod> getMethods() {
		Set<HttpMethod> allowMethods = new HashSet<>();
		allowMethods.add(HttpMethod.GET);
		allowMethods.add(HttpMethod.POST);
		allowMethods.add(HttpMethod.DELETE);
		allowMethods.add(HttpMethod.PATCH);
		allowMethods.add(HttpMethod.PUT);
		return allowMethods;
	}

	private void completeStartup(AsyncResult<HttpServer> http, Promise<Void> fut) {
		if (http.succeeded()) {
			fut.complete();
		} else {
			fut.fail(http.cause());
		}
	}

	@Override
	public void stop() throws Exception {
		mongo.close();
	}

}
