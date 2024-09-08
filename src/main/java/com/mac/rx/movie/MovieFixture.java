package com.mac.rx.movie;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import static com.mac.rx.movie.MovieRepository.COLLECTION;

public class MovieFixture {
    public static void createMovie(MongoClient mongo, Handler<AsyncResult<Void>> next, Promise<Void> fut) {
        Movie shawshank = new Movie("The Shawshank Redemption", "9.3");
        Movie godfather = new Movie("The Godfather", "9.2");

        mongo.count(COLLECTION, new JsonObject(), count -> {
            if (count.succeeded()) {
                if (count.result() == 0) {

                    mongo.insert(COLLECTION, shawshank.toJson(), ar -> {
                        if (ar.failed()) {
                            fut.fail(ar.cause());
                        } else {
                            mongo.insert(COLLECTION, godfather.toJson(), ar2 -> {
                                if (ar2.failed()) {
                                    fut.fail("Failed trying yo create element");
                                } else {
                                    next.handle(Future.succeededFuture());
                                }
                            });
                        }
                    });
                } else {
                    next.handle(Future.succeededFuture());
                }
            } else {
                // report the error
                fut.fail(count.cause());
            }
        });
    }
}
